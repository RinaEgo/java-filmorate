package javaFilmorate;

import javaFilmorate.controller.FilmController;
import javaFilmorate.exception.ValidationException;
import javaFilmorate.model.Film;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FilmControllerTest {
    FilmController filmController = new FilmController();

    @Test
    void shouldCreateFilm() {
        Film film = new Film(1,
                "Film",
                "Comedy",
                LocalDate.of(2020, 1, 1),
                120);

        filmController.createFilm(film);

        List<Film> testList = filmController.findAllFilms();

        assertEquals(1, testList.size(), "Количество фильмов некорректно.");
        assertEquals(film.getId(), testList.get(0).getId(), "ID не совпадает.");
        assertEquals(film.getName(), testList.get(0).getName(), "Название не совпадает.");
        assertEquals(film.getDescription(), testList.get(0).getDescription(), "Описание не совпадает.");
        assertEquals(film.getReleaseDate(), testList.get(0).getReleaseDate(), "Дата не совпадает.");
        assertEquals(film.getDuration(), testList.get(0).getDuration(), "Продолжительность не совпадает.");
    }

    @Test
    void shouldNotCreateFilmWithNoName() {
        Film film = new Film(1,
                "",
                "Comedy",
                LocalDate.of(2020, 1, 1),
                120);

        Throwable thrown = assertThrows(ValidationException.class, () -> filmController.createFilm(film));
        assertNotNull(thrown.getMessage());
        assertEquals("Название введено некорректно.", thrown.getMessage());

        List<Film> testList = filmController.findAllFilms();
        assertEquals(0, testList.size(), "Был добавлен фильм с некорректными данными.");
    }

    @Test
    void shouldNotCreateFilmWithBlancName() {
        Film film = new Film(1,
                " ",
                "Comedy",
                LocalDate.of(2020, 1, 1),
                120);

        Throwable thrown = assertThrows(ValidationException.class, () -> filmController.createFilm(film));
        assertNotNull(thrown.getMessage());
        assertEquals("Название введено некорректно.", thrown.getMessage());

        List<Film> testList = filmController.findAllFilms();
        assertEquals(0, testList.size(), "Был добавлен фильм с некорректными данными.");
    }

    @Test
    void shouldNotCreateFilmWithHugeDescription() {
        Film film = new Film(1,
                "Film",
                "toomanysymbolstoomanysymbolstoomanysymbolstoomanysymbolstoomanysymbolstoomanysymbols" +
                        "toomanysymbolstoomanysymbolstoomanysymbolstoomanysymbolstoomanysymbolstoomanysymbols" +
                        "toomanysymbolstoomanysymbolstoomanysymbolstoomanysymbolstoomanysymbolstoomanysymbols",
                LocalDate.of(2020, 1, 1),
                120);

        Throwable thrown = assertThrows(ValidationException.class, () -> filmController.createFilm(film));
        assertNotNull(thrown.getMessage());
        assertEquals("Превышена максимальная длина описания.", thrown.getMessage());

        List<Film> testList = filmController.findAllFilms();
        assertEquals(0, testList.size(), "Был добавлен фильм с некорректными данными.");
    }

    @Test
    void shouldNotCreateTooOldFilm() {
        Film film = new Film(1,
                "Film",
                "Comedy",
                LocalDate.of(1000, 1, 1),
                120);

        Throwable thrown = assertThrows(ValidationException.class, () -> filmController.createFilm(film));
        assertNotNull(thrown.getMessage());
        assertEquals("Дата релиза раньше допустимой.", thrown.getMessage());

        List<Film> testList = filmController.findAllFilms();
        assertEquals(0, testList.size(), "Был добавлен фильм с некорректными данными.");
    }

    @Test
    void shouldNotCreateFilmWithNegativeDuration() {
        Film film = new Film(1,
                "Film",
                "Comedy",
                LocalDate.of(2020, 1, 1),
                -120);

        Throwable thrown = assertThrows(ValidationException.class, () -> filmController.createFilm(film));
        assertNotNull(thrown.getMessage());
        assertEquals("Продолжительность фильма должна быть положительной.", thrown.getMessage());

        List<Film> testList = filmController.findAllFilms();
        assertEquals(0, testList.size(), "Был добавлен фильм с некорректными данными.");
    }

    @Test
    void shouldNotCreateFilmWithZeroDuration() {
        Film film = new Film(1,
                "Film",
                "Comedy",
                LocalDate.of(2020, 1, 1),
                0);

        Throwable thrown = assertThrows(ValidationException.class, () -> filmController.createFilm(film));
        assertNotNull(thrown.getMessage());
        assertEquals("Продолжительность фильма должна быть положительной.", thrown.getMessage());

        List<Film> testList = filmController.findAllFilms();
        assertEquals(0, testList.size(), "Был добавлен фильм с некорректными данными.");
    }


}
