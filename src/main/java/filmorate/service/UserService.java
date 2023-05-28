package filmorate.service;

import filmorate.exception.NotFoundException;
import filmorate.model.User;
import filmorate.storage.UserStorage;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public User validateUser(Integer id) {
        if (!userStorage.findAllUsers().contains(getUserById(id))) {
            throw new NotFoundException("Пользователь с ID " + id + " не найден.");
        } else {
            return getUserById(id);
        }
    }

    public User getUserById(Integer userId) {
        return userStorage.getUserById(userId);
    }

    public List<User> findAllUsers() {
        return userStorage.findAllUsers();
    }

    public void addFriend(Integer userId, Integer friendId) {
        validateUser(userId);
        validateUser(friendId);

        userStorage.addFriend(userId, friendId);
    }

    public void deleteFriend(Integer userId, Integer friendId) {
        validateUser(userId);
        validateUser(friendId);

        userStorage.deleteFriend(userId, friendId);
    }

    public List<User> getFriendsList(Integer userId) {
        validateUser(userId);

        return userStorage.getFriendsList(userId);
    }

    public List<User> getCommonFriends(Integer userId, Integer otherUserId) {
        validateUser(userId);
        validateUser(otherUserId);

        List<User> userFriends = userStorage.getFriendsList(userId);

        List<User> otherUserFriends = userStorage.getFriendsList(otherUserId);

        if (userFriends.isEmpty() || otherUserFriends.isEmpty()) {
            return new ArrayList<>();
        }

        userFriends.retainAll(otherUserFriends);

        return userFriends.stream()
                .sorted(Comparator.comparingInt(User::getId))
                .collect(Collectors.toList());
    }
}
