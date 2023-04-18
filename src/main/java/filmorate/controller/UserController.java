package filmorate.controller;

import filmorate.exception.ValidationException;
import filmorate.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final static Logger log = LoggerFactory.getLogger(UserController.class);
    private final Map<Integer, User> users = new HashMap<>();
    private int userId = 1;

    @GetMapping()
    public List<User> findAllUsers() {
        return new ArrayList<>(users.values());
    }

    @PostMapping()
    public User createUser(@RequestBody User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        if (users.containsKey(user.getId())) {
            log.warn("Пользователь уже существует.");
            throw new ValidationException("Пользователь уже существует.");
        } else if (user.getEmail().isBlank() || user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            log.warn("Email введён некорректно.");
            throw new ValidationException("Email введён некорректно.");
        } else if (user.getLogin().isBlank() || user.getLogin().isEmpty()) {
            log.warn("Логин введён некорректно.");
            throw new ValidationException("Логин введён некорректно.");
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Дата рождения указана некорректно.");
            throw new ValidationException("Дата рождения указана некорректно.");
        } else {
            user.setId(userId);
            users.put(user.getId(), user);
            userId++;
            log.info("Пользователь {} добавлен.", user);
        }
        return user;
    }

    @PutMapping()
    public User updateUser(@RequestBody User user) { //todo add logs

        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.info("Пользователь {} добавлен.", user);
        } else {
            log.warn("Пользователь не был зарегистрирован.");
            throw new ValidationException("Пользователь не был зарегистрирован.");
        }
        return user;
    }
}
