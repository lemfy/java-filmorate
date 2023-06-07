package ru.yandex.practicum.filmorate.storage.dao;

import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("DBUserStorage")
@Primary
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

    private User mapToRow(SqlRowSet rowSet) {
        int id = rowSet.getInt("id");
        String email = rowSet.getString("email");
        String login = rowSet.getString("login");
        String name = rowSet.getString("name");
        LocalDate birthday = rowSet.getDate("birthday").toLocalDate();
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
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql);
        while (rowSet.next()) {
            User user = mapToRow(rowSet);
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
}