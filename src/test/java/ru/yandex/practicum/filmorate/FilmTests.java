package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controllers.FilmController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class FilmTests {
    FilmController filmController;

    Film film1 = new Film(1, "AAA", "AAA",
            LocalDate.of(2000, 1, 1), 1, null);
    Film film2 = new Film(2, "BBB", "BBB",
            LocalDate.of(2000, 1, 2), 2, null);
    Film film3 = new Film(3, "", "CCC",
            LocalDate.of(2000, 1, 3), 3, null);
    Film film4 = new Film(4, "DDD", "DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD" +
            "DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD" +
            "DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD",
            LocalDate.of(2000, 1, 4), 4, null);
    Film film5 = new Film(5, "EEE", "EEE",
            LocalDate.of(1000, 1, 5), 5, null);
    Film film6 = new Film(6, "FFF", "FFF",
            LocalDate.of(2000, 1, 5), 0, null);
    Film film7 = new Film(7, "GGG", "DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD" +
            "DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD" +
            "DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD",
            LocalDate.of(2000, 1, 7), 7, null);
    Film film8 = new Film(8, "GGG", "GGG",
            LocalDate.of(1895, 12, 28), 5, null);
    Film film9 = new Film(9, "HHH", "HHH",
            LocalDate.of(2000, 1, 9), -10, null);

    @Test
    void shouldAddFilm() {
        filmController.createFilm(film1);
        assertTrue(filmController.findAllFilms().contains(film1));
    }

    @Test
    void shouldReturnAllFilms() {
        assertEquals(0, filmController.findAllFilms().size());
        filmController.createFilm(film1);
        filmController.createFilm(film2);
        Collection<Film> films = filmController.findAllFilms();
        assertEquals(2, films.size());
        assertTrue(filmController.findAllFilms().contains(film1));
        assertTrue(filmController.findAllFilms().contains(film2));
    }

    @Test
    void shouldNotAddFilmIfNameIsEmpty() {
        assertThrows(ValidationException.class, () -> filmController.createFilm(film3));
        assertFalse(filmController.findAllFilms().contains(film3));
    }

    @Test
    void shouldNotAddFilmIfDescriptionIsTooFar() {
        assertThrows(ValidationException.class, () -> filmController.createFilm(film4));
        assertFalse(filmController.findAllFilms().contains(film4));
    }

    @Test
    void shouldNotAddFilmIfDateOfReleaseIsTooEarly() {
        assertThrows(ValidationException.class, () -> filmController.createFilm(film5));
        assertFalse(filmController.findAllFilms().contains(film5));
    }

    @Test
    void shouldNotAddFilmIfDurationsIsZero() {
        assertThrows(ValidationException.class, () -> filmController.createFilm(film6));
        assertFalse(filmController.findAllFilms().contains(film6));
    }

    @Test
    void shouldNotAddFilmIfDescriptionIfGood() {
        filmController.createFilm(film7);
        assertTrue(filmController.findAllFilms().contains(film7));
    }

    @Test
    void shouldNotAddFilmIfDateOfReleaseIsGood() {
        filmController.createFilm(film8);
        assertTrue(filmController.findAllFilms().contains(film8));
    }

    @Test
    void shouldNotAddFilmIfDurationsIsNegative() {
        assertThrows(ValidationException.class, () -> filmController.createFilm(film6));
        assertFalse(filmController.findAllFilms().contains(film6));
    }
}