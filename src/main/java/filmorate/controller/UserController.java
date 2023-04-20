package filmorate.controller;

import filmorate.exception.ValidationException;
import filmorate.model.User;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private int userId = 1;

    @GetMapping()
    public List<User> findAllUsers() {
        return new ArrayList<>(users.values());
    }

    @PostMapping()
    public User createUser(@Valid @RequestBody User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        if (users.containsKey(user.getId())) {
            log.warn("Пользователь уже существует.");
            throw new ValidationException("Пользователь уже существует.");
        } else {
            user.setId(userId);
            users.put(user.getId(), user);
            userId++;
            log.info("Пользователь {} добавлен.", user);
        }
        return user;
    }

    @PutMapping()
    public User updateUser(@Valid @RequestBody User user) {

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
