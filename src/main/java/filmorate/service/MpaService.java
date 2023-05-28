package filmorate.service;

import filmorate.exception.NotFoundException;
import filmorate.model.Mpa;
import filmorate.storage.MpaStorage;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MpaService {

    private final MpaStorage mpaStorage;

    public MpaService(MpaStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    public List<Mpa> getAllMpa() {
        return mpaStorage.getAllMpa();
    }

    public Mpa getMpaById(Integer id) {
        return mpaStorage.getMpaById(id).orElseThrow(() ->
                new NotFoundException(String.format(
                        "MPA с ID = %s не существует.", id)));
    }
}
