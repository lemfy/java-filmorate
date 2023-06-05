package ru.yandex.practicum.filmorate.storage.dao;

import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("DBUserStorage")
public class DBUserStorage implements UserStorage {
    private final Logger log = (Logger) LoggerFactory.getLogger(DBUserStorage.class);
    private final JdbcTemplate jdbcTemplate;

    public DBUserStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<User> userRowMapper = (ResultSet resultSet, int rowNum) -> User.builder()
            .id(resultSet.getInt("id"))
            .login(resultSet.getString("login"))
            .email(resultSet.getString("email"))
            .name(resultSet.getString("name"))
            .birthday(resultSet.getDate("birthday").toLocalDate())
            .build();

    private User mapToRow(SqlRowSet sqlRowSet) {
        int id = sqlRowSet.getInt("id");
        String email = sqlRowSet.getString("email");
        String login = sqlRowSet.getString("login");
        String name = sqlRowSet.getString("name");
        LocalDate birthday = sqlRowSet.getDate("birthday").toLocalDate();
        return User.builder()
                .id(id)
                .email(email)
                .login(login)
                .name(name)
                .birthday(birthday)
                .build();
    }

    @Override
    public User createUser(User user) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        Map<String, Object> par = new HashMap<>();
        par.put("email", user.getEmail());
        par.put("login", user.getLogin());
        par.put("name", user.getName());
        par.put("birthday", user.getBirthday());
        Number userKey = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(par));
        user.setId(userKey.intValue());
        return user;
    }

    @Override
    public User changeUser(int id, User user) {
        String updateSql = "update users set email = ?, login = ?, name = ?, birthday = ? where id = ?";
        if (jdbcTemplate.update(updateSql,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId()
        ) == 0) {
            throw new UserNotFoundException("Пользователь не найден");
        } else {
            return user;
        }
    }

    @Override
    public List<User> findAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "select id, email, login, name, birthday  from users";
        SqlRowSet RowSet = jdbcTemplate.queryForRowSet(sql);
        while (RowSet.next()) {
            User user = mapToRow(RowSet);
            users.add(user);
        }
        return users;
    }

    @Override
    public User findUserById(int id) {
        String query = "select id,login,email,name, birthday from users where id = ?";
        try {
            return jdbcTemplate.queryForObject(query, userRowMapper, id);
        } catch (Exception e) {
            throw new UserNotFoundException("Пользователь не найден!");
        }
    }

    @Override
    public void addFriend(int userId, int friendId) {
        boolean friendAccepted;
        String sqlGetReversFriend = "select * from friends " +
                "where UserID = ? and FriendID = ?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sqlGetReversFriend, friendId, userId);
        friendAccepted = rowSet.next();
        String sqlSetFriend = "insert into friends (UserID, FriendID, Status) " +
                "values (?,?,?)";
        jdbcTemplate.update(sqlSetFriend, userId, friendId, friendAccepted);
        if (friendAccepted) {
            String sqlStatus = "update friends set Status = true " +
                    "where UserID = ? and FriendID = ?";
            jdbcTemplate.update(sqlStatus, friendId, userId);
        }
    }

    @Override
    public void removeFriend(int userId, int friendId) {
        String sqlRemoveFriend = "delete from FRIENDSHIP where USERID = ? and FRIENDID = ?";
        jdbcTemplate.update(sqlRemoveFriend, userId, friendId);
        String sqlStatus = "update FRIENDSHIP set STATUS = false " +
                "where USERID = ? and FRIENDID = ?";
        jdbcTemplate.update(sqlStatus, friendId, userId);
    }
}