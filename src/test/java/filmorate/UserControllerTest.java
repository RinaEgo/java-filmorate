package filmorate;

import filmorate.controller.UserController;
import filmorate.model.User;
import filmorate.service.UserService;
import filmorate.storage.user.InMemoryUserStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

class UserControllerTest {
    ValidatorFactory factory;
    private Validator validator;
    @Autowired
    UserController userController = new UserController(new UserService(new InMemoryUserStorage()));

    @BeforeEach
    public void beforeEach() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testCreateUser() {
        User user = new User(1,
                "mail@mail.ru",
                "dolore",
                "Nick Name",
                LocalDate.of(1946, 8, 20));

        userController.createUser(user);

        List<User> testList = userController.findAllUsers();

        assertEquals(1, testList.size(), "Количество пользователей некорректно.");
        assertEquals(user.getId(), testList.get(0).getId(), "ID не совпадает.");
        assertEquals(user.getEmail(), testList.get(0).getEmail(), "Email не совпадает.");
        assertEquals(user.getLogin(), testList.get(0).getLogin(), "Login не совпадает.");
        assertEquals(user.getName(), testList.get(0).getName(), "Имя не совпадает.");
        assertEquals(user.getBirthday(), testList.get(0).getBirthday(), "Дата рождения не совпадает.");
    }

    @Test
    void testFailCreateUserWithNoEmail() {
        User user = new User(2,
                "",
                "dolore",
                "Nick Name",
                LocalDate.of(1946, 8, 20));

        assertEquals(1, validator.validate(user).size(), "Некорректная работа с ошибочными данными.");

        List<User> testList = userController.findAllUsers();
        assertEquals(0, testList.size(), "Был добавлен пользователь с некорректными данными.");
    }

    @Test
    void testFailCreateUserWithWrongEmail() {
        User user = new User(2,
                "wrong.com",
                "dolore",
                "Nick Name",
                LocalDate.of(1946, 8, 20));

        assertEquals(1, validator.validate(user).size(), "Некорректная работа с ошибочными данными.");

        List<User> testList = userController.findAllUsers();
        assertEquals(0, testList.size(), "Был добавлен пользователь с некорректными данными.");
    }

    @Test
    void testFailCreateUserWithNoLogin() {
        User user = new User(2,
                "mail@mail.ru",
                "",
                "Nick Name",
                LocalDate.of(1946, 8, 20));

        assertEquals(1, validator.validate(user).size(), "Некорректная работа с ошибочными данными.");

        List<User> testList = userController.findAllUsers();
        assertEquals(0, testList.size(), "Был добавлен пользователь с некорректными данными.");
    }

    @Test
    void testFailCreateUserWithBlankLogin() {
        User user = new User(2,
                "mail@mail.ru",
                " ",
                "Nick Name",
                LocalDate.of(1946, 8, 20));

        assertEquals(1, validator.validate(user).size(), "Некорректная работа с ошибочными данными.");

        List<User> testList = userController.findAllUsers();
        assertEquals(0, testList.size(), "Был добавлен пользователь с некорректными данными.");
    }

    @Test
    void testCreateUserWithNoName() {
        User user = new User(2,
                "mail@mail.ru",
                "Login",
                "",
                LocalDate.of(1946, 8, 20));

        userController.createUser(user);
        List<User> testList = userController.findAllUsers();

        assertEquals(user.getLogin(), testList.get(0).getName(), "Имя не совпадает.");
    }

    @Test
    void testFailCreateUserWithWrongBirthDate() {
        User user = new User(3,
                "mail@mail.ru",
                "dolore",
                "Nick Name",
                LocalDate.of(2046, 8, 20));

        assertEquals(1, validator.validate(user).size(), "Некорректная работа с ошибочными данными.");

        List<User> testList = userController.findAllUsers();
        assertEquals(0, testList.size(), "Был добавлен пользователь с некорректными данными.");
    }
}
