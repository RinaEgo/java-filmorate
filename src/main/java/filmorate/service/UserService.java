package filmorate.service;

import filmorate.exception.NotFoundException;
import filmorate.model.User;
import filmorate.storage.user.UserStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
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

        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);

        user.getFriendsIds().add(friendId);
        friend.getFriendsIds().add(userId);
    }

    public void deleteFriend(Integer userId, Integer friendId) {
        validateUser(userId);
        validateUser(friendId);

        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);

        if (user.getFriendsIds().contains(friendId)) {
            user.getFriendsIds().remove(friendId);
            friend.getFriendsIds().remove(userId);
        } else {
            throw new NotFoundException("Пользователь с ID " + friendId + " не найден в списке друзей.");
        }
    }

    public List<User> getFriendsList(Integer userId) {
        validateUser(userId);

        User user = userStorage.getUserById(userId);

        List<User> friends = new ArrayList<>();

        if (!user.getFriendsIds().isEmpty()) {

            for (Integer id : user.getFriendsIds()) {
                friends.add(getUserById(id));
            }
        }
        return friends;
    }

    public List<User> getCommonFriends(Integer userId, Integer otherUserId) {
        validateUser(userId);
        validateUser(otherUserId);

        List<User> commonFriends = new ArrayList<>();

        if (!getFriendsList(userId).isEmpty()) {

            for (User friend : getFriendsList(userId)) {
                if (getFriendsList(otherUserId).contains(friend)) {
                    commonFriends.add(friend);
                }
            }
        }
        return commonFriends;
    }
}
