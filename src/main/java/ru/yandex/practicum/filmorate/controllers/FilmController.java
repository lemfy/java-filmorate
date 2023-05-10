package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private int id = 1;
    private final Map<Integer, Film> films = new HashMap<>();


    @GetMapping
    public List<Film> allFilms() {
        log.debug("List size {}", films.size());
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film create(@NotNull @RequestBody @Valid Film film) {
        log.info("Post request {}", film);
        validate(film);
        film.setId(id++);
        films.put(film.getId(), film);
        log.info("Film added {}", film);
        return film;
    }

    @PutMapping
    public Film change(@NotNull @RequestBody @Valid Film film) {
        log.info("Put request: {}", film);
        if (films.containsKey(film.getId())) {
            validate(film);
            films.put(film.getId(), film);
            log.info("Film changed {}", film);
        } else {
            log.debug("Key not found {}", film.getId());
            throw new ValidationException("Фильм не найден");
        }
        return film;
    }

    public void validate(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.error("Film releaseDate is invalid. {}", film.getName());
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года");
        }
        if (film.getName() == null || film.getName().isEmpty() || film.getName().isBlank()) {
            log.error("Film name is empty or invalid. {}", film.getId());
            throw new ValidationException("Имя не может быть пустым");
        }
        if ((film.getDescription().length()) > 200) {
            log.error("Film description is too long. {}", film.getName());
            throw new ValidationException("Описание фильма больше 200 символов");
        }
        if (film.getDuration() <= 0) {
            log.error("Film duration is negative. {}", film.getName());
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }
    }
}