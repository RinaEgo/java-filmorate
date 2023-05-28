package filmorate.service;

import filmorate.exception.NotFoundException;
import filmorate.model.Genre;
import filmorate.storage.GenreStorage;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenreService {
    private final GenreStorage genreStorage;

    public GenreService(GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    public List<Genre> getAllGenres() {
        return genreStorage.getAllGenres();
    }

    public Genre getGenreById(Integer id) {
        return genreStorage.getGenreById(id).orElseThrow(() ->
                new NotFoundException(String.format(
                        "Жанр с ID = %s не существует.", id
                )));
    }
}
