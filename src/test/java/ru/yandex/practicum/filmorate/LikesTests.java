package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Likes;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.LikesStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor__ = @Autowired)
public class LikesTests {

    private final LikesStorage likesStorage;
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final JdbcTemplate jdbcTemplate;

    @Test
    void testAddAndRemoveLikes() {
        Mpa mpa = new Mpa(1, "R");
        mpa.setId(1);
        int filmId = filmStorage.createFilm(new Film(1, "name", "desc", LocalDate.of(2000, 1, 1), 1, mpa)).getId();
        int userId = userStorage.createUser(new User(1, "e@mail.ru", "login", "name", LocalDate.of(1990, 1, 1))).getId();

        assertEquals(0, jdbcTemplate.query("select userId from likes where filmId = ?",
                (rs, rn) -> rs.getInt("userId"), userId).size());

        likesStorage.addLike(new Likes(filmId, userId));
        assertEquals(1, jdbcTemplate.query("select userId from likes where filmId = ?",
                (rs, rn) -> rs.getInt("userId"), filmId).size());
        assertEquals(userId, jdbcTemplate.query("select userId from likes where filmId = ?",
                (rs, rn) -> rs.getInt("userId"), filmId).get(0));

        likesStorage.removeLike(new Likes(filmId, userId));
        assertEquals(0, jdbcTemplate.query("select userId from likes where filmId = ?",
                (rs, rn) -> rs.getInt("userId"), filmId).size());
    }

    @Test
    void testGetAllLikes() {
        Mpa mpa = new Mpa(1, "R");
        mpa.setId(1);
        int filmId = filmStorage.createFilm(new Film(1, "name", "desc", LocalDate.of(2000, 1, 1), 1, mpa)).getId();
        int userId = userStorage.createUser(new User(1, "e@mail.ru", "login", "name", LocalDate.of(1990, 1, 1))).getId();
        int userId2 = userStorage.createUser(new User(2, "e@mail.ru", "login", "name", LocalDate.of(1990, 1, 2))).getId();

        likesStorage.addLike(new Likes(filmId, userId));
        likesStorage.addLike(new Likes(filmId, userId2));
        assertEquals(2, jdbcTemplate.query("select userId from likes where filmId = ?",
                (rs, rn) -> rs.getInt("userId"), filmId).size());
        assertEquals(userId, jdbcTemplate.query("select userId from likes where filmId = ?",
                (rs, rn) -> rs.getInt("userId"), filmId).get(0));
    }

    @Test
    void testGetLikesWithFilmId() {
        Mpa mpa = new Mpa(1, "R");
        mpa.setId(1);
        int filmId = filmStorage.createFilm(new Film(1, "name", "desc", LocalDate.of(2000, 1, 1), 1, mpa)).getId();
        int userId = userStorage.createUser(new User(1, "e@mail.ru", "login", "name", LocalDate.of(1990, 1, 1))).getId();

        Likes like = new Likes(filmId, userId);
        likesStorage.addLike(like);
        assertEquals(1, jdbcTemplate.query("select userId from likes where filmId = ?",
                (rs, rn) -> rs.getInt("userId"), filmId).size());
        assertEquals(userId, jdbcTemplate.query("select userId from likes where filmId = ?",
                (rs, rn) -> rs.getInt("userId"), filmId).get(0));
        System.out.println(likesStorage.getLikesWithFilmId(1));
        assertThat(likesStorage.getLikesWithFilmId(filmId).contains(like));
    }
}