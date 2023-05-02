package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controllers.FilmController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class FilmTests {
    FilmController filmController = new FilmController();

    Film film1 = new Film("AAA", "AAA",
            LocalDate.of(2000, 1, 1), 1);
    Film film2 = new Film("BBB", "BBB",
            LocalDate.of(2000, 1, 2), 2);
    Film film3 = new Film("", "CCC",
            LocalDate.of(2000, 1, 3), 3);
    Film film4 = new Film("DDD", "DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD" +
            "DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD" +
            "DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD",
            LocalDate.of(2000, 1, 4), 4);
    Film film5 = new Film("EEE", "EEE",
            LocalDate.of(1000, 1, 5), 5);
    Film film6 = new Film("FFF", "FFF",
            LocalDate.of(2000, 1, 5), 0);
    Film film7 = new Film("GGG", "DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD" +
            "DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD" +
            "DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD",
            LocalDate.of(2000, 1, 7), 7);
    Film film8 = new Film("GGG", "GGG",
            LocalDate.of(1895, 12, 28), 5);
    Film film9 = new Film("HHH", "HHH",
            LocalDate.of(2000, 1, 9), -10);

    @Test
    void shouldAddFilm() {
        filmController.create(film1);
        assertTrue(filmController.allFilms().contains(film1));
    }

    @Test
    void shouldReturnAllFilms() {
        assertEquals(0, filmController.allFilms().size());
        filmController.create(film1);
        filmController.create(film2);
        Collection<Film> films = filmController.allFilms();
        assertEquals(2, films.size());
        assertTrue(filmController.allFilms().contains(film1));
        assertTrue(filmController.allFilms().contains(film2));
    }

    @Test
    void shouldNotAddFilmIfNameIsEmpty() {
        assertThrows(ValidationException.class, () -> filmController.create(film3));
        assertFalse(filmController.allFilms().contains(film3));
    }

    @Test
    void shouldNotAddFilmIfDescriptionIsTooFar() {
        assertThrows(ValidationException.class, () -> filmController.create(film4));
        assertFalse(filmController.allFilms().contains(film4));
    }

    @Test
    void shouldNotAddFilmIfDateOfReleaseIsTooEarly() {
        assertThrows(ValidationException.class, () -> filmController.create(film5));
        assertFalse(filmController.allFilms().contains(film5));
    }

    @Test
    void shouldNotAddFilmIfDurationsIsZero() {
        assertThrows(ValidationException.class, () -> filmController.create(film6));
        assertFalse(filmController.allFilms().contains(film6));
    }

    @Test
    void shouldNotAddFilmIfDescriptionIfGood() {
        filmController.create(film7);
        assertTrue(filmController.allFilms().contains(film7));
    }

    @Test
    void shouldNotAddFilmIfDateOfReleaseIsGood() {
        filmController.create(film8);
        assertTrue(filmController.allFilms().contains(film8));
    }

    @Test
    void shouldNotAddFilmIfDurationsIsNegative() {
        assertThrows(ValidationException.class, () -> filmController.create(film6));
        assertFalse(filmController.allFilms().contains(film6));
    }
}