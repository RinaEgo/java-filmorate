package filmorate.storage;

import filmorate.model.User;

import java.util.List;

public interface UserStorage {
    User getUserById(Integer userId);

    List<User> findAllUsers();

    User createUser(User user);

    User updateUser(User user);

    void addFriend(Integer userId, Integer friendId);

    void deleteFriend(Integer userId, Integer friendId);

    List<User> getFriendsList(Integer userId);
}
