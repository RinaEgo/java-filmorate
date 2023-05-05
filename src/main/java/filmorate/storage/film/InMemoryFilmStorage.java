package filmorate.storage.film;

import filmorate.exception.NotFoundException;
import filmorate.exception.ValidationException;
import filmorate.model.Film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private int filmId = 1;

    @Override
    public Film getFilmById(Integer filmId) {
        if (films.containsKey(filmId)) {
            return films.get(filmId);
        } else {
            log.warn("Фильма не существует.");
            throw new NotFoundException("Фильма не существует.");
        }
    }

    @Override
    public List<Film> findAllFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film createFilm(Film film) {

        if (films.containsKey(film.getId())) {
            log.warn("Фильм уже существует.");
            throw new ValidationException("Фильм уже существует.");
        } else if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("Дата релиза раньше допустимой.");
            throw new ValidationException("Дата релиза раньше допустимой.");
        } else {
            film.setId(filmId);
            films.put(film.getId(), film);
            filmId++;
        }
        return film;
    }

    @Override
    public Film updateFilm(Film film) {

        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.info("Фильм {} добавлен.", film);
        } else {
            log.warn("Фильма не существует.");
            throw new NotFoundException("Фильма не существует.");
        }
        return film;
    }
}
