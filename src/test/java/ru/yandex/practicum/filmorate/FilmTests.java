package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.dao.*;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor__ = @Autowired)
public class FilmTests {
    final DBFilmStorage filmStorage;
    final DBFilmGenreStorage filmGenreStorage;
    final DBLikesStorage likeStorage;
    final DBUserStorage userStorage;
    final DBMpaStorage mpaStorage;


    @Test
    void testCreateFilm() {
        Film film1 = new Film(1, "AAA", "AAA",
                LocalDate.of(2000, 1, 1), 1, new Mpa(1, "G"));
        final Film film = filmStorage.createFilm(film1);

        assertThat(film).isNotNull();
        assertThat(film.getId()).isNotZero();
        assertThat(film.getName()).isEqualTo(film1.getName());
        assertThat(film.getDescription()).isEqualTo(film1.getDescription());
        assertThat(film.getReleaseDate()).isEqualTo(film1.getReleaseDate());
        assertThat(film.getDuration()).isEqualTo(film1.getDuration());
        assertThat(film.getMpa()).isEqualTo(film1.getMpa());
    }

    @Test
    void testChangeFilm() {
        Film film1 = new Film(1, "AAA", "AAA",
                LocalDate.of(2000, 1, 1), 1, new Mpa(1, "G"));
        Film film2 = new Film(2, "BBB", "BBB",
                LocalDate.of(2000, 1, 2), 2, new Mpa(2, "PG"));
        final Film saved = filmStorage.createFilm(film1);
        final int filmId = saved.getId();
        film2.setId(filmId);

        final Film updated = filmStorage.changeFilm(film2);

        assertThat(updated).isNotNull();
        assertThat(updated.getId()).isEqualTo(filmId);
        assertThat(updated.getName()).isEqualTo(film2.getName());
        assertThat(updated.getDescription()).isEqualTo(film2.getDescription());
        assertThat(updated.getReleaseDate()).isEqualTo(film2.getReleaseDate());
        assertThat(updated.getDuration()).isEqualTo(film2.getDuration());
        assertThat(updated.getMpa()).isEqualTo(film2.getMpa());
    }

    @Test
    void findAllFilms() {
        Film film3 = filmStorage.createFilm(new Film(3, "CCC", "CCC",
                LocalDate.of(2000, 1, 3), 3, new Mpa(1, null)));
        Film film4 = filmStorage.createFilm(new Film(4, "DDD", "DDD",
                LocalDate.of(2000, 1, 4), 3, new Mpa(2, null)));

        List<Film> allFilms = filmStorage.findAllFilms();
        System.out.println(allFilms);

        assertThat(allFilms).isNotNull();
        assertThat(allFilms.size()).isEqualTo(6);
        assertThat(allFilms.get(4)).isEqualTo(film3);
        assertThat(allFilms.get(5)).isEqualTo(film4);
    }

    @Test
    void testFindById() {
        Film film5 = filmStorage.createFilm(new Film(5, "EEE", "EEE",
                LocalDate.of(2000, 1, 5), 5, new Mpa(5, null)));
        filmStorage.createFilm(film5);
        Film createdFilm = filmStorage.findFilmById(2);

        assertThat(createdFilm).isEqualTo(film5);
    }
}