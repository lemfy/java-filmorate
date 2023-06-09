package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.FilmGenre;

import java.util.List;

public interface FilmGenreStorage {
    FilmGenre add(FilmGenre filmGenre);

    void deleteByFilmId(int filmId);

    List<FilmGenre> getLikesFilmId(int filmId);

    List<FilmGenre> getAllFilmGenre();
}