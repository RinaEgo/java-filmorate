package filmorate;

import filmorate.exception.ValidationException;
import filmorate.model.Film;

import filmorate.model.Mpa;
import filmorate.model.User;
import filmorate.service.FilmService;
import filmorate.service.UserService;
import filmorate.storage.impl.GenreDbStorage;
import filmorate.storage.impl.MpaDbStorage;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class FilmServiceTest {
    ValidatorFactory factory;
    private Validator validator;

    private final FilmService filmService;
    private final UserService userService;
    private final MpaDbStorage mpaDbStorage;
    private final GenreDbStorage genreDbStorage;

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
                120, new Mpa(1, "G"));

        filmService.createFilm(film);

        List<Film> testList = filmService.findAllFilms();

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
                120, new Mpa(1, "G"));

        assertEquals(1, validator.validate(film).size(), "Некорректная работа с ошибочными данными.");

        List<Film> testList = filmService.findAllFilms();
        assertEquals(0, testList.size(), "Был добавлен фильм с некорректными данными.");
    }

    @Test
    void testFailCreateFilmWithBlancName() {
        Film film = new Film(1,
                " ",
                "Comedy",
                LocalDate.of(2020, 1, 1),
                120, new Mpa(1, "G"));

        assertEquals(1, validator.validate(film).size(), "Некорректная работа с ошибочными данными.");

        List<Film> testList = filmService.findAllFilms();
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
                120, new Mpa(1, "G"));

        assertEquals(1, validator.validate(film).size(), "Некорректная работа с ошибочными данными.");

        List<Film> testList = filmService.findAllFilms();
        assertEquals(0, testList.size(), "Был добавлен фильм с некорректными данными.");
    }

    @Test
    void testFailCreateTooOldFilm() {
        Film film = new Film(1,
                "Film",
                "Comedy",
                LocalDate.of(1000, 1, 1),
                120, new Mpa(1, "G"));

        Throwable thrown = assertThrows(ValidationException.class, () -> filmService.createFilm(film));
        assertNotNull(thrown.getMessage(), "Исключение выбрасывается некорректно.");
        assertEquals("Дата релиза раньше допустимой.", thrown.getMessage(), "Выброс исключения работает некорректно.");

        List<Film> testList = filmService.findAllFilms();
        assertEquals(0, testList.size(), "Был добавлен фильм с некорректными данными.");
    }

    @Test
    void testFailCreateFilmWithNegativeDuration() {
        Film film = new Film(1,
                "Film",
                "Comedy",
                LocalDate.of(2020, 1, 1),
                -120, new Mpa(1, "G"));

        assertEquals(1, validator.validate(film).size(), "Некорректная работа с ошибочными данными.");

        List<Film> testList = filmService.findAllFilms();
        assertEquals(0, testList.size(), "Был добавлен фильм с некорректными данными.");
    }

    @Test
    void testFailCreateFilmWithZeroDuration() {
        Film film = new Film(1,
                "Film",
                "Comedy",
                LocalDate.of(2020, 1, 1),
                0, new Mpa(1, "G"));

        assertEquals(1, validator.validate(film).size(), "Некорректная работа с ошибочными данными.");

        List<Film> testList = filmService.findAllFilms();
        assertEquals(0, testList.size(), "Был добавлен фильм с некорректными данными.");
    }

    @Test
    public void testAddLike() {
        Film film = new Film(1,
                "Film",
                "Comedy",
                LocalDate.of(2020, 1, 1),
                120, new Mpa(1, "G"));

        User user = new User(1,
                "email",
                "login",
                "name",
                LocalDate.of(2010, 8, 15));

        userService.createUser(user);

        filmService.createFilm(film);

        filmService.addLike(1, 1);

        assertEquals(1, filmService.getFilmById(1).getLikes().size(), "Лайки добавляются некорректно.");
    }

    @Test
    public void testDeleteLike() {
        Film film = new Film(1,
                "Film",
                "Comedy",
                LocalDate.of(2020, 1, 1),
                120, new Mpa(1, "G"));

        User user = new User(1,
                "email",
                "login",
                "name",
                LocalDate.of(2010, 8, 15));

        userService.createUser(user);

        filmService.createFilm(film);

        filmService.addLike(1, 1);
        filmService.deleteLike(1, 1);

        assertEquals(0, filmService.getFilmById(1).getLikes().size(), "Лайки удаляются некорректно.");
    }
}
