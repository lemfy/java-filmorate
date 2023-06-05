package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Genres;

import java.util.List;

public interface GenreStorage {
    Genres findGenreById(int id);

    List<Genres> getAllGenres();
}