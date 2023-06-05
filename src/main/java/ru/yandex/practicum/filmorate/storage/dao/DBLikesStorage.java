package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Likes;
import ru.yandex.practicum.filmorate.storage.LikesStorage;

import java.util.HashSet;
import java.util.Set;

@Component
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
        var Query = "delete from Likes where UserID = ? AND FilmID = ?";
        jdbcTemplate.update(Query, likes.getUserId(), likes.getFilmId());
    }

    @Override
    public Set<Likes> getAllLikes() {
        Set<Likes> likes = new HashSet<>();
        SqlRowSet RowSet = jdbcTemplate.queryForRowSet("select UserID, FilmID from Likes");
        while (RowSet.next()) {
            Likes like = mapToRow(RowSet);
            likes.add(like);
        }
        return likes;
    }

    @Override
    public Set<Likes> getLikesWithFilmId(int filmId) {
        Set<Likes> likes = new HashSet<>();
        SqlRowSet RowSet = jdbcTemplate.queryForRowSet("select UserID, FilmID from Likes where FilmID = ?", filmId);
        while (RowSet.next()) {
            var like = mapToRow(RowSet);
            likes.add(like);
        }
        return likes;
    }

    @Override
    public Likes getLikesCurrentUserWithFilmId(int userId, int filmId) {
        SqlRowSet RowSet = jdbcTemplate.queryForRowSet("select UserID, FilmID from Likes where UserID = ? AND FilmID = ?", userId, filmId);
        if (RowSet.next()) {
            return mapToRow(RowSet);
        } else {
            return null;
        }
    }

    private Likes mapToRow(SqlRowSet RowSet) {
        int userId = RowSet.getInt("UserID");
        int filmId = RowSet.getInt("FilmID");
        return Likes.builder()
                .userId(userId)
                .filmId(filmId)
                .build();
    }
}