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
    public Friends addFriend(Friends Friends) {
        String sql = "insert into Friends (UserID, FriendID, Status) values(?, ?, ?)";
        jdbcTemplate.update(sql,
                Friends.getUserId(),
                Friends.getFriendId(),
                Friends.isStatus());
        return Friends;
    }

    @Override
    public Friends update(Friends Friends) {
        String updateSql = "update Friends set Status = ? where UserID = ? AND FriendID = ?";
        if (jdbcTemplate.update(updateSql,
                Friends.isStatus(),
                Friends.getUserId(),
                Friends.getFriendId())
                <= 0) {
            throw new UserNotFoundException("Пользователя не существует");
        } else {
            return Friends;
        }
    }

    @Override
    public boolean deleteFriend(Friends Friends) {
        String sql = "delete from Friends where UserID=? AND FriendID=?";
        return (jdbcTemplate.update(sql, Friends.getUserId(), Friends.getFriendId()) > 0);
    }

    @Override
    public List<Friends> findAllFriends(int id) {
        List<Friends> friends = new ArrayList<>();
        String sql = "select UserID, FriendID, Status from Friends where UserID = ? OR (FriendID = ? AND Status = ?)";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, id, id, true);
        while (rowSet.next()) {
            Friends Friends = mapToRow(rowSet);
            friends.add(Friends);
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

    private Friends mapToRow(SqlRowSet rowSet) {
        int userId = rowSet.getInt("UserID");
        int FriendID = rowSet.getInt("FriendID");
        boolean status = rowSet.getBoolean("Status");
        return Friends.builder()
                .userId(userId)
                .friendId(FriendID)
                .status(status)
                .build();
    }
}