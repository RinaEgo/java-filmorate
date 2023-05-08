package filmorate.storage.film;

import filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    Film getFilmById(Integer filmId);

    List<Film> findAllFilms();

    Film createFilm(Film film);

    Film updateFilm(Film film);
}
