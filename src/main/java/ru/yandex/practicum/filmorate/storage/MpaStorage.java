package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Set;

public interface MpaStorage {
    Mpa getMpaById(int id);

    Set<Mpa> getAllMpa();
}