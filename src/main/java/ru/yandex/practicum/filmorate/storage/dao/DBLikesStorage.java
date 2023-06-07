package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Likes;
import ru.yandex.practicum.filmorate.storage.LikesStorage;

import java.util.HashSet;
import java.util.Set;

@Repository
public class DBLikesStorage extends DbStorage implements LikesStorage {
    public DBLikesStorage(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public Likes addLike(Likes likes) {
        String sql = "insert into Likes (UserID, FilmID) values(?, ?)";
        jdbcTemplate.update(sql,
                likes.getUserId(),
                likes.getFilmId());
        return likes;
    }

    @Override
    public void removeLike(Likes likes) {
        var query = "delete from Likes where UserID = ? AND FilmID = ?";
        jdbcTemplate.update(query, likes.getUserId(), likes.getFilmId());
    }

    @Override
    public Set<Likes> getAllLikes() {
        Set<Likes> likes = new HashSet<>();
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select UserID, FilmID from Likes");
        while (rowSet.next()) {
            Likes like = mapToRow(rowSet);
            likes.add(like);
        }
        return likes;
    }

    @Override
    public Set<Likes> getLikesWithFilmId(int filmId) {
        Set<Likes> likes = new HashSet<>();
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select UserID, FilmID from Likes where FilmID = ?", filmId);
        while (rowSet.next()) {
            var like = mapToRow(rowSet);
            likes.add(like);
        }
        return likes;
    }

    @Override
    public Likes getLikesCurrentUserWithFilmId(int userId, int filmId) {
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select UserID, FilmID from Likes where UserID = ? AND FilmID = ?", userId, filmId);
        if (rowSet.next()) {
            return mapToRow(rowSet);
        } else {
            return null;
        }
    }

    private Likes mapToRow(SqlRowSet rowSet) {
        int userId = rowSet.getInt("UserID");
        int filmId = rowSet.getInt("FilmID");
        return Likes.builder()
                .userId(userId)
                .filmId(filmId)
                .build();
    }
}