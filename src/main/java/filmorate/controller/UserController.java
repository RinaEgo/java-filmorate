package filmorate.controller;

import filmorate.model.User;
import filmorate.service.UserService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> findAllUsers() {
        return userService.findAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Integer id) {
        return userService.getUserById(id);
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        return userService.createUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        return userService.updateUser(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        userService.addFriend(id, friendId);
        log.info("Пользователь с ID = " + id + " добавил в друзья пользователя с ID = " + friendId + ".");
    }

    @DeleteMapping("{id}/friends/{friendId}")
    public void deleteFriendById(@PathVariable Integer id, @PathVariable Integer friendId) {
        userService.deleteFriend(id, friendId);
        log.info("Пользователь с ID = " + id + " удалил из друзей пользователя с ID = " + friendId + ".");
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriendByID(@PathVariable Integer id) {
        return userService.getFriendsList(id);
    }

    @GetMapping("/{id}/friends/common/{friendId}")
    public List<User> getCommonFriends(@PathVariable Integer id, @PathVariable Integer friendId) {
        return userService.getCommonFriends(id, friendId);
    }
}
