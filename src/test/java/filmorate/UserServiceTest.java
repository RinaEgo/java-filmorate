package filmorate;

import filmorate.model.User;
import filmorate.service.UserService;
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

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserServiceTest {
    ValidatorFactory factory;
    private Validator validator;
    private final UserService userService;

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

        userService.createUser(user);

        List<User> testList = userService.findAllUsers();

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

        List<User> testList = userService.findAllUsers();
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

        List<User> testList = userService.findAllUsers();
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

        List<User> testList = userService.findAllUsers();
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

        List<User> testList = userService.findAllUsers();
        assertEquals(0, testList.size(), "Был добавлен пользователь с некорректными данными.");
    }

    @Test
    void testCreateUserWithNoName() {
        User user = new User(2,
                "mail@mail.ru",
                "Login",
                "",
                LocalDate.of(1946, 8, 20));

        userService.createUser(user);
        List<User> testList = userService.findAllUsers();

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

        List<User> testList = userService.findAllUsers();
        assertEquals(0, testList.size(), "Был добавлен пользователь с некорректными данными.");
    }

    @Test
    public void testAddFriend() {
        User first = new User(4,
                "loginOne",
                "nameOne",
                "email@email.ru",
                LocalDate.of(1990, 12, 12));

        User second = new User(5,
                "loginTwo",
                "nameTwo",
                "yandex@yandex.ru",
                LocalDate.of(1995, 5, 5));


        userService.createUser(first);
        userService.createUser(second);

        userService.addFriend(first.getId(), second.getId());
        assertEquals(1, userService.getFriendsList(first.getId()).size(), "Пользователь добавлен в друзья некорректно.");
    }

    @Test
    public void testGetFriendsList() {
        User first = userService.createUser(new User(6,
                "mail@mail.ru",
                "dolore",
                "Nick Name",
                LocalDate.of(1980, 8, 20)));

        User second = userService.createUser(new User(7,
                "mmail@mail.ru",
                "nedolore",
                "NickName",
                LocalDate.of(1951, 8, 20)));

        User third = userService.createUser(new User(8,
                "mmmail@mail.ru",
                "tridolore",
                "Nick",
                LocalDate.of(2000, 8, 20)));

        userService.addFriend(first.getId(), second.getId());
        userService.addFriend(first.getId(), third.getId());

        List<User> friends = new ArrayList<>(Arrays.asList(second, third));
        assertEquals(friends, userService.getFriendsList(first.getId()), "Пользователи добавлены в друзья некорректно.");
    }

    @Test
    public void testDeleteFromFriend() {
        User first = userService.createUser(new User(9,
                "mail@mail.ru",
                "dolore",
                "Nick Name",
                LocalDate.of(1945, 8, 20)));

        User second = userService.createUser(new User(10,
                "mmail@mail.ru",
                "nedolore",
                "NickName",
                LocalDate.of(2011, 8, 20)));

        userService.addFriend(first.getId(), second.getId());
        userService.deleteFriend(first.getId(), second.getId());
        assertEquals(0, userService.getFriendsList(first.getId()).size(), "Удаление из друзей работает некорректно.");
    }
}
