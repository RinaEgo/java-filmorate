package filmorate.storage.impl;

import filmorate.exception.NotFoundException;
import filmorate.exception.ValidationException;
import filmorate.model.Film;
import filmorate.model.Genre;
import filmorate.model.Mpa;
import filmorate.storage.FilmStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film getFilmById(Integer filmId) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT * FROM FILMS f LEFT JOIN MPA m " +
                "ON f.rating_id = m.rating_id WHERE f.id = ?", filmId);

        if (filmRows.next()) {
            Film film = new Film(filmRows.getInt("id"),
                    filmRows.getString("name"),
                    filmRows.getString("description"),
                    filmRows.getDate("release_date").toLocalDate(),
                    filmRows.getInt("duration"),
                    new Mpa(filmRows.getInt("rating_id"), filmRows.getString("rating_name")));

            String sqlQueryGetGenres = "SELECT GENRE_NAME AS name, fg.GENRE_ID AS id " +
                    "FROM film_genre fg " +
                    "LEFT JOIN GENRES g on g.genre_id = fg.genre_id " +
                    "WHERE fg.film_id = ? " +
                    "ORDER BY g.genre_id";

            List<Genre> genresFilms = jdbcTemplate.query(sqlQueryGetGenres, this::mapRowToGenre, filmId);

            film.setGenres(new LinkedHashSet<>(genresFilms));

            String sqlQueryGetLikes = "SELECT user_id " +
                    "FROM likes " +
                    "WHERE film_id = ? " +
                    "ORDER BY user_id";

            List<Integer> likesFilm = jdbcTemplate.query(sqlQueryGetLikes,
                    (rs, rowNum) -> rs.getInt(1),
                    filmId
            );

            film.setLikes(new LinkedHashSet<>(likesFilm));

            return film;
        } else {
            throw new NotFoundException("Фильма не существует.");
        }
    }

    @Override
    public List<Film> findAllFilms() {
        List<Film> films = new ArrayList<>();

        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT * FROM FILMS f " +
                "LEFT JOIN MPA m ON f.rating_id = m.rating_id " +
                "ORDER BY f.id");

        while (filmRows.next()) {
            films.add(new Film(
                    filmRows.getInt("id"),
                    filmRows.getString("name"),
                    filmRows.getString("description"),
                    filmRows.getDate("release_date").toLocalDate(),
                    filmRows.getInt("duration"),
                    new Mpa(filmRows.getInt("rating_id"), filmRows.getString("rating_name"))));
        }

        Map<Integer, Film> mapFilms = films.stream()
                .collect(Collectors.toMap(Film::getId, Function.identity()));

        String sqlQueryGetGenres = "SELECT film_id, genre_name, fg.genre_id " +
                "FROM film_genre fg " +
                "LEFT JOIN GENRES g ON fg.genre_id = g.genre_id " +
                "ORDER BY film_id";

        List<Map<String, Object>> genresFilms = jdbcTemplate.queryForList(sqlQueryGetGenres);

        genresFilms.forEach(
                t -> mapFilms.get(Integer.parseInt(t.get("film_id").toString())).getGenres().add(
                        new Genre(Integer.parseInt(t.get("genre_id").toString()),
                                t.get("genre_name").toString())
                ));

        String sqlQueryGetLikes = "SELECT user_id, film_id " +
                "FROM likes " +
                "ORDER BY film_id, user_id";

        List<Map<String, Object>> likesFilms = jdbcTemplate.queryForList(sqlQueryGetLikes);

        likesFilms.forEach(
                t -> mapFilms.get(Integer.parseInt(t.get("film_id").toString())).getLikes().add(
                        Integer.parseInt(t.get("user_id").toString())
                ));

        return films;
    }

    @Override
    public Film createFilm(Film film) {

        if (findAllFilms().contains(film)) {
            throw new ValidationException("Фильм уже существует.");
        } else if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза раньше допустимой.");
        }
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("FILMS")
                .usingGeneratedKeyColumns("id");

        int id = insert.executeAndReturnKey(film.toMap()).intValue();
        film.setId(id);

        setFilmGenre(film);

        return getFilmById(id);
    }

    @Override
    public Film updateFilm(Film film) {
        if (!findAllFilms().contains(getFilmById(film.getId()))) {
            throw new NotFoundException("Фильма не существует.");
        }
        String sql = "UPDATE FILMS SET name = ?, description = ?, release_date = ?, duration = ?, rating_id = ? " +
                "WHERE id = ?";

        jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());

        String sqlQueryDeleteGenres = "DELETE FROM film_genre " +
                "WHERE film_id = ?";

        jdbcTemplate.update(sqlQueryDeleteGenres, film.getId());

        setFilmGenre(film);

        updateFilmLikes(film);

        return film;
    }

    private void setFilmGenre(Film film) {
        if (film.getGenres() == null || film.getGenres().isEmpty()) {
            return;
        }
        String sqlQueryAddGenres = "INSERT INTO film_genre (film_id, genre_id) " +
                "VALUES (?, ?)";

        List<Genre> genres = new ArrayList<>(film.getGenres());

        jdbcTemplate.batchUpdate(sqlQueryAddGenres, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setInt(1, film.getId());
                ps.setInt(2, genres.get(i).getId());
            }

            @Override
            public int getBatchSize() {
                return genres.size();
            }
        });
    }

    private void updateFilmLikes(Film film) {
        String sqlQueryDeleteLikes = "DELETE FROM likes " +
                "WHERE film_id = ?";

        jdbcTemplate.update(sqlQueryDeleteLikes, film.getId());

        String sqlQueryAddLikes = "INSERT INTO likes (film_id, user_id) " +
                "VALUES (?, ?)";

        List<Integer> likes = new ArrayList<>(film.getLikes());

        jdbcTemplate.batchUpdate(sqlQueryAddLikes, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, film.getId());
                ps.setLong(2, likes.get(i));
            }

            @Override
            public int getBatchSize() {
                return likes.size();
            }
        });
    }

    private Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return new Genre(
                resultSet.getInt("genre_id"),
                resultSet.getString("genre_name")
        );
    }
}
