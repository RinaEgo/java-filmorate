package filmorate;

import filmorate.model.Mpa;
import filmorate.service.MpaService;
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
class MpaServiceTest {

    private final MpaService mpaService;

    @Test
    void testGetAllMpa() {
        List<Mpa> mpa = new ArrayList<>();
        mpa.add(new Mpa(1, "G"));
        mpa.add(new Mpa(2, "PG"));
        mpa.add(new Mpa(3, "PG-13"));
        mpa.add(new Mpa(4, "R"));
        mpa.add(new Mpa(5, "NC-17"));

        assertEquals(mpa, mpaService.getAllMpa(), "Получение списка рейтингов работает некорректно.");
    }

    @Test
    void testGetMpaById() {
        assertEquals("G", mpaService.getMpaById(1).getName(), "Получение рейтинга по id работает некорректно.");
    }
}