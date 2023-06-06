package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Friends;
import ru.yandex.practicum.filmorate.storage.FriendsStorage;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Component
public class DBFriendsStorage extends DbStorage implements FriendsStorage {

    public DBFriendsStorage(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    private final RowMapper<Friends> FriendsRowMapper = (ResultSet resultSet, int rowNum) -> Friends.builder()
            .userId(resultSet.getInt("UserID"))
            .friendId(resultSet.getInt("FriendID"))
            .status(resultSet.getBoolean("Status"))
            .build();


    @Override
    public Friends addFriend(Friends friends) {
        String sql = "insert into Friends (UserID, FriendID, Status) values(?, ?, ?)";
        jdbcTemplate.update(sql,
                friends.getUserId(),
                friends.getFriendId(),
                friends.isStatus());
        return friends;
    }

    @Override
    public Friends update(Friends friends) {
        String updateSql = "update Friends set Status = ? where UserID = ? AND FriendID = ?";
        if (jdbcTemplate.update(updateSql,
                friends.isStatus(),
                friends.getUserId(),
                friends.getFriendId())
                <= 0) {
            throw new UserNotFoundException("Пользователя не существует");
        } else {
            return friends;
        }
    }

    @Override
    public boolean deleteFriend(Friends friends) {
        String sql = "delete from Friends where UserID=? AND FriendID=?";
        return (jdbcTemplate.update(sql, friends.getUserId(), friends.getFriendId()) > 0);
    }

    @Override
    public List<Friends> findAllFriends(int id) {
        List<Friends> friends = new ArrayList<>();
        String sql = "select UserID, FriendID, Status from Friends where UserID = ? OR (FriendID = ? AND Status = ?)";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, id, id, true);
        while (sqlRowSet.next()) {
            Friends Friends1 = mapToRow(sqlRowSet);
            friends.add(Friends1);
        }
        return friends;
    }


    @Override
    public Friends findCommonFriends(int user1Id, int user2Id) {
        String query = "select UserID, FriendID, Status from Friends where UserID = ? AND FriendID = ? OR UserID = ? AND FriendID = ?";
        try {
            return jdbcTemplate.queryForObject(query, FriendsRowMapper, user1Id, user2Id, user2Id, user1Id);
        } catch (Exception e) {
            return null;
        }
    }

    private Friends mapToRow(SqlRowSet sqlRowSet) {
        int userId = sqlRowSet.getInt("UserID");
        int friendID = sqlRowSet.getInt("FriendID");
        boolean status = sqlRowSet.getBoolean("Status");
        return Friends.builder()
                .userId(userId)
                .friendId(friendID)
                .status(status)
                .build();
    }
}