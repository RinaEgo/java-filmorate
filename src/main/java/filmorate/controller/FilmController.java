package filmorate.controller;

import filmorate.model.Film;
import filmorate.service.FilmService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping()
    public List<Film> findAllFilms() {
        return filmService.findAllFilms();
    }

    @PostMapping()
    public Film createFilm(@Valid @RequestBody Film film) {
        return filmService.createFilm(film);
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable Integer id) {
        return filmService.getFilmById(id);
    }

    @PutMapping()
    public Film updateFilm(@Valid @RequestBody Film film) {
        return filmService.updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable Integer id, @PathVariable Integer userId) {
        filmService.addLike(id, userId);
        log.info("Пользователь с ID = " + userId + " поставил лайк фильму с ID = " + id + ".");
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable Integer id, @PathVariable Integer userId) {
        filmService.deleteLike(id, userId);
        log.info("Пользователь с ID = " + userId + " удалил лайк у фильма с ID = " + id + ".");
    }

    @GetMapping("/popular")
    public List<Film> getMostPopularFilms(@RequestParam(required = false, defaultValue = "10") Integer count) {
        return filmService.getMostPopularFilms(count);
    }
}
