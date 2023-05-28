package filmorate.storage.impl;

import filmorate.exception.NotFoundException;
import filmorate.exception.ValidationException;
import filmorate.model.User;
import filmorate.storage.UserStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User getUserById(Integer userId) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM USERS WHERE id = ?", userId);
        if (userRows.next()) {
            return new User(
                    userRows.getInt("id"),
                    userRows.getString("email"),
                    userRows.getString("login"),
                    userRows.getString("name"),
                    userRows.getDate("birthday").toLocalDate());
        } else {
            throw new NotFoundException("Пользователь не был зарегистрирован.");
        }
    }

    @Override
    public List<User> findAllUsers() {
        List<User> users = new ArrayList<>();

        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM USERS");
        while (userRows.next()) {
            users.add(new User(
                    userRows.getInt("id"),
                    userRows.getString("email"),
                    userRows.getString("login"),
                    userRows.getString("name"),
                    userRows.getDate("birthday").toLocalDate()));
        }
        return users;
    }

    @Override
    public User createUser(User user) {

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (findAllUsers().contains(user)) {
            throw new ValidationException("Пользователь уже существует.");
        } else {
            Map<String, Object> params = new HashMap<>();
            params.put("id", user.getId());
            params.put("email", user.getEmail());
            params.put("login", user.getLogin());
            params.put("name", user.getName());
            params.put("birthday", user.getBirthday());

            SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate)
                    .withTableName("USERS")
                    .usingGeneratedKeyColumns("id");

            int id = insert.executeAndReturnKey(params).intValue();

            user.setId(id);

            return user;
        }
    }

    @Override
    public User updateUser(User user) {
        if (!findAllUsers().contains(getUserById(user.getId()))) {
            throw new NotFoundException("Фильма не существует.");
        }
        String sql = "UPDATE USERS SET email = ?, login = ?, name = ?, birthday = ? " +
                "WHERE id = ?";
        jdbcTemplate.update(sql,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());
        return user;
    }

    @Override
    public void addFriend(Integer userId, Integer friendId) {
        String sqlQueryAddFriend = "INSERT INTO friends (user_id, friend_id) " +
                "VALUES (?, ?)";
        jdbcTemplate.update(sqlQueryAddFriend, userId, friendId);
    }

    @Override
    public void deleteFriend(Integer userId, Integer friendId) {
        String sqlQueryDeleteFriend = "DELETE FROM friends " +
                "WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sqlQueryDeleteFriend, userId, friendId);
    }

    @Override
    public List<User> getFriendsList(Integer userId){
        String sqlQueryGetFriends = "SELECT * " +
                "FROM USERS " +
                "INNER JOIN friends f ON USERS.id = f.friend_id " +
                "WHERE f.USER_ID = ? " +
                "ORDER BY USERS.id";

        return jdbcTemplate.query(sqlQueryGetFriends, this::mapRowToUser, userId);
    }

    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        return new User(
                resultSet.getInt("id"),
                resultSet.getString("email"),
                resultSet.getString("login"),
                resultSet.getString("name"),
                resultSet.getDate("birthday").toLocalDate()
        );
    }
}
