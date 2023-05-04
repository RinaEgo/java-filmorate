package filmorate.service;

import filmorate.exception.NotFoundException;
import filmorate.model.Film;
import filmorate.storage.film.FilmStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserService userService;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserService userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
    }

    public Film getFilmById(Integer filmId) {
        return filmStorage.getFilmById(filmId);
    }

    public List<Film> findAllFilms() {
        return filmStorage.findAllFilms();
    }

    public Film createFilm(Film film) {
        return filmStorage.createFilm(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public Film validateFilm(Integer id) {
        if (!filmStorage.findAllFilms().contains(getFilmById(id))) {
            throw new NotFoundException("Фильм с ID " + id + " не найден.");
        } else {
            return getFilmById(id);
        }
    }

    public void addLike(Integer filmId, Integer userId) {
        validateFilm(filmId);
        userService.validateUser(userId);

        Film film = filmStorage.getFilmById(filmId);

        film.getLikes().add(userId);
    }

    public void deleteLike(Integer filmId, Integer userId) {
        validateFilm(filmId);
        userService.validateUser(userId);

        Film film = filmStorage.getFilmById(filmId);

        if (film.getLikes().contains(userId)) {
            film.getLikes().remove(userId);
        } else {
            throw new NotFoundException("Лайк пользователя с ID " + userId + " не найден.");
        }
    }

    public List<Film> getMostPopularFilms(Integer count) {
        if (filmStorage.findAllFilms().isEmpty()) {
            throw new NotFoundException("Список фильмов пуст.");
        } else {
            return filmStorage.findAllFilms().stream()
                    .sorted(Comparator.comparingInt(f -> -f.getLikes().size()))
                    .limit(count).collect(Collectors.toList());
        }
    }
}
