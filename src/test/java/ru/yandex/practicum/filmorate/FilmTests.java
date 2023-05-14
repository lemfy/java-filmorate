package ru.yandex.practicum.filmorate;

import ru.yandex.practicum.filmorate.controllers.FilmController;

public class FilmTests {
    FilmController filmController;
/*
    Film film1 = new Film(1, "AAA", "AAA",
            LocalDate.of(2000, 1, 1), 1, new HashSet<>());
    Film film2 = new Film(2, "BBB", "BBB",
            LocalDate.of(2000, 1, 2), 2, new HashSet<>());
    Film film3 = new Film(3, "", "CCC",
            LocalDate.of(2000, 1, 3), 3, new HashSet<>());
    Film film4 = new Film(4, "DDD", "DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD" +
            "DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD" +
            "DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD",
            LocalDate.of(2000, 1, 4), 4, new HashSet<>());
    Film film5 = new Film(5, "EEE", "EEE",
            LocalDate.of(1000, 1, 5), 5, new HashSet<>());
    Film film6 = new Film(6, "FFF", "FFF",
            LocalDate.of(2000, 1, 5), 0, new HashSet<>());
    Film film7 = new Film(7, "GGG", "DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD" +
            "DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD" +
            "DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD",
            LocalDate.of(2000, 1, 7), 7, new HashSet<>());
    Film film8 = new Film(8, "GGG", "GGG",
            LocalDate.of(1895, 12, 28), 5, new HashSet<>());

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

 */
}