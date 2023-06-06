package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    Film createFilm(Film film);

    Film changeFilm(Film film);

    List<Film> findAllFilms();

    Film findFilmById(int id);
}