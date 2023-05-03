package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private int id = 1;
    private final Map<Integer, Film> films = new HashMap<>();


    @GetMapping
    public List<Film> allFilms() {
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film create(@RequestBody @Valid Film film) {
        validate(film);
        film.setId(id++);
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film change(@RequestBody @Valid Film film) {
        validate(film);
        film.setId(film.getId());
        films.put(film.getId(), film);
        return film;
    }

    public void validate(Film film) {
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