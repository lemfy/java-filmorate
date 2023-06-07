package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final UserStorage userStorage;
    private int id = 1;
    private final Map<Integer, Film> films = new HashMap<>();

    @Autowired
    public InMemoryFilmStorage(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public Film createFilm(Film film) {
        film.setId(id++);
        films.put(film.getId(), film);
        film.setLikes(new HashSet<>());
        log.info("film added");
        return films.get(film.getId());
    }

    @Override
    public Film changeFilm(int id, Film film) {
        if (!films.containsKey(film.getId())) {
            throw new FilmNotFoundException(String.format("Фильма с id %d не существует.", id));
        }
        films.put(film.getId(), film);
        log.info("film changed");
        return films.get(film.getId());
    }

    @Override
    public List<Film> findAllFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film findFilmById(int id) {
        if (!films.containsKey(id)) {
            throw new FilmNotFoundException(String.format("Фильма с id %d не существует.", id));
        }
        log.info("currently film found");
        return films.get(id);
    }

    @Override
    public Film addLike(int filmId, int userId) {
        if (!films.containsKey(filmId)) {
            throw new FilmNotFoundException(String.format("Фильма с id %d не существует.", filmId));
        }
        if (!userStorage.findAllUsers().contains(userStorage.findUserById(userId))) {
            throw new FilmNotFoundException(String.format("Пользователь с id %d не существует.", userId));
        }
        Film film = films.get(filmId);
        film.getLikes().add(userId);
        films.put(film.getId(), film);
        log.info("like added");
        return films.get(filmId);
    }

    @Override
    public Film removeLike(int filmId, int userId) {
        if (!films.containsKey(filmId)) {
            throw new FilmNotFoundException(String.format("Фильма с id %d не существует.", filmId));
        }
        if (!userStorage.findAllUsers().contains(userStorage.findUserById(userId))) {
            throw new FilmNotFoundException(String.format("Пользователь с id %d не существует.", userId));
        }
        Film film = films.get(filmId);
        film.getLikes().remove(userId);
        films.put(film.getId(), film);
        log.info("like removed");
        return films.get(filmId);
    }

    @Override
    public List<Film> getBestFilms(int count) {
        List<Film> topFilms = findAllFilms();
        log.info("best films list found");
        return findAllFilms().stream().sorted(Comparator.comparingInt(Film::getLikeSize).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }
}