package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    Film createFilm(Film film);

    Film changeFilm(int id, Film film);

    List<Film> findAllFilms();

    Film findFilmById(int id);

    Film addLike(int filmId, int userId);

    Film removeLike(int filmId, int userId);

    List<Film> getBestFilms(int count);
}