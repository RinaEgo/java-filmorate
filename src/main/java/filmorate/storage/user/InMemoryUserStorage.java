package filmorate.storage.user;

import filmorate.exception.NotFoundException;
import filmorate.exception.ValidationException;
import filmorate.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private int userId = 1;

    @Override
    public User getUserById(Integer userId) {
        if (users.containsKey(userId)) {
            return users.get(userId);
        } else {
            log.warn("Пользователь не был зарегистрирован.");
            throw new NotFoundException("Пользователь не был зарегистрирован.");
        }
    }

    @Override
    public List<User> findAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User createUser(User user) {
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

    @Override
    public User updateUser(User user) {

        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.info("Пользователь {} добавлен.", user);
        } else {
            log.warn("Пользователь не был зарегистрирован.");
            throw new NotFoundException("Пользователь не был зарегистрирован.");
        }
        return user;
    }
}
