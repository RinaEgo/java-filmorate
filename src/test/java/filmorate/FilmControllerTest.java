package filmorate;

import filmorate.controller.FilmController;
import filmorate.exception.ValidationException;
import filmorate.model.Film;

import filmorate.service.FilmService;
import filmorate.service.UserService;
import filmorate.storage.film.InMemoryFilmStorage;
import filmorate.storage.user.InMemoryUserStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    ValidatorFactory factory;
    private Validator validator;
    @Autowired
    FilmController filmController = new FilmController(new FilmService(new InMemoryFilmStorage(),
            new UserService(new InMemoryUserStorage())));

    @BeforeEach
    public void beforeEach() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testCreateFilm() {
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
    void testFailCreateFilmWithNoName() {
        Film film = new Film(1,
                "",
                "Comedy",
                LocalDate.of(2020, 1, 1),
                120);

        assertEquals(1, validator.validate(film).size(), "Некорректная работа с ошибочными данными.");

        List<Film> testList = filmController.findAllFilms();
        assertEquals(0, testList.size(), "Был добавлен фильм с некорректными данными.");
    }

    @Test
    void testFailCreateFilmWithBlancName() {
        Film film = new Film(1,
                " ",
                "Comedy",
                LocalDate.of(2020, 1, 1),
                120);

        assertEquals(1, validator.validate(film).size(), "Некорректная работа с ошибочными данными.");

        List<Film> testList = filmController.findAllFilms();
        assertEquals(0, testList.size(), "Был добавлен фильм с некорректными данными.");
    }

    @Test
    void testFailCreateFilmWithHugeDescription() {
        Film film = new Film(1,
                "Film",
                "toomanysymbolstoomanysymbolstoomanysymbolstoomanysymbolstoomanysymbolstoomanysymbols" +
                        "toomanysymbolstoomanysymbolstoomanysymbolstoomanysymbolstoomanysymbolstoomanysymbols" +
                        "toomanysymbolstoomanysymbolstoomanysymbolstoomanysymbolstoomanysymbolstoomanysymbols",
                LocalDate.of(2020, 1, 1),
                120);

        assertEquals(1, validator.validate(film).size(), "Некорректная работа с ошибочными данными.");

        List<Film> testList = filmController.findAllFilms();
        assertEquals(0, testList.size(), "Был добавлен фильм с некорректными данными.");
    }

    @Test
    void testFailCreateTooOldFilm() {
        Film film = new Film(1,
                "Film",
                "Comedy",
                LocalDate.of(1000, 1, 1),
                120);

        Throwable thrown = assertThrows(ValidationException.class, () -> filmController.createFilm(film));
        assertNotNull(thrown.getMessage(), "Исключение выбрасывается некорректно.");
        assertEquals("Дата релиза раньше допустимой.", thrown.getMessage(), "Выброс исключения работает некорректно.");

        List<Film> testList = filmController.findAllFilms();
        assertEquals(0, testList.size(), "Был добавлен фильм с некорректными данными.");
    }

    @Test
    void testFailCreateFilmWithNegativeDuration() {
        Film film = new Film(1,
                "Film",
                "Comedy",
                LocalDate.of(2020, 1, 1),
                -120);

        assertEquals(1, validator.validate(film).size(), "Некорректная работа с ошибочными данными.");

        List<Film> testList = filmController.findAllFilms();
        assertEquals(0, testList.size(), "Был добавлен фильм с некорректными данными.");
    }

    @Test
    void testFailCreateFilmWithZeroDuration() {
        Film film = new Film(1,
                "Film",
                "Comedy",
                LocalDate.of(2020, 1, 1),
                0);

        assertEquals(1, validator.validate(film).size(), "Некорректная работа с ошибочными данными.");

        List<Film> testList = filmController.findAllFilms();
        assertEquals(0, testList.size(), "Был добавлен фильм с некорректными данными.");
    }
}
