package ru.yandex.practicum.filmorate;

import ru.yandex.practicum.filmorate.controllers.FilmController;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.HashSet;

public class FilmTests {
    FilmController filmController;

    Film film = new Film(1, "1", "1", LocalDate.now(), 10, new HashSet<>());

}