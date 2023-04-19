package filmorate.controller;

import filmorate.exception.ValidationException;
import filmorate.model.Film;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private int filmId = 1;

    @GetMapping()
    public List<Film> findAllFilms() {
        return new ArrayList<>(films.values());
    }

    @PostMapping()
    public Film createFilm(@Valid @RequestBody Film film) {

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
            log.info("Фильм {} добавлен.", film);
        }
        return film;
    }

    @PutMapping()
    public Film updateFilm(@Valid @RequestBody Film film) {

        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.info("Фильм {} добавлен.", film);
        } else {
            log.warn("Фильма не существует.");
            throw new ValidationException("Фильма не существует.");
        }
        return film;
    }
}
