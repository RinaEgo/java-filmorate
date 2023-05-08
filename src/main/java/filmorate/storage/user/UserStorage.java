package filmorate.storage.user;

import filmorate.model.User;

import java.util.List;

public interface UserStorage {
    User getUserById(Integer userId);

    List<User> findAllUsers();

    User createUser(User user);

    User updateUser(User user);
}
