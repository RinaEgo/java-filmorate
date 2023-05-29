package filmorate;

import filmorate.model.Genre;
import filmorate.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class GenreServiceTest {

    private final GenreService genreService;

    @Test
    void testGetAllGenres() {
        List<Genre> genres = new ArrayList<>();
        genres.add(new Genre(1, "Комедия"));
        genres.add(new Genre(2, "Драма"));
        genres.add(new Genre(3, "Мультфильм"));
        genres.add(new Genre(4, "Триллер"));
        genres.add(new Genre(5, "Документальный"));
        genres.add(new Genre(6, "Боевик"));

        assertEquals(genres, genreService.getAllGenres(), "Получение списка жанров работает некорректно.");
    }

    @Test
    void testGetGenreById() {
        assertEquals("Комедия", genreService.getGenreById(1).getName(), "Получение жанра по id работает некорректно.");
    }
}