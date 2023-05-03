package ru.yandex.practicum.filmorate.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
public class FilmController {
    private final static Logger filmLog = LoggerFactory.getLogger(FilmController.class);
    int id = 1;
    private final Map<String, Film> films = new HashMap<>();


    @GetMapping("/films")
    public Collection<Film> allFilms() {
        return films.values();
    }

    @PostMapping("/film")
    public Film create(@RequestBody @Valid Film film) {
        Validation(film);
        film.setId(id++);
        films.put(film.getName(), film);
        return film;
    }

    @PutMapping("/film")
    public Film change(@RequestBody @Valid Film film) {
        Validation(film);
        film.setId(film.getId());
        films.put(film.getName(), film);
        return film;
    }

    public void Validation(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года");
        }
        if (film.getName().isBlank()) {
            throw new ValidationException("Имя не может быть пустым");
        }
        if ((film.getDescription().length()) > 200) {
            throw new ValidationException("Описание фильма больше 200 символов");
        }
        if (film.getDurations() <= 0) {
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }
    }
}