package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    private int id = 1;
    private final Map<Integer, Film> films = new HashMap<>();


    @GetMapping("/films")
    public List<Film> findAllFilms() {
        return filmService.findAllFilms();
    }

    @PostMapping("/films")
    public Film createFilm(@NotNull @RequestBody @Valid Film film) {
        log.info("Post request {}", film);
        validate(film);
        log.info("Film added {}", film);
        return filmService.createFilm(film);
    }

    @PutMapping("/films")
    public Film changeFilm(@NotNull @RequestBody @Valid Film film) {
        log.info("Put request: {}", film);
        validate(film);
        log.info("Film changed {}", film);
        return filmService.changeFilm(film.getId(), film);
    }

    @GetMapping("/films/{id}")
    public Film findFilmById(@PathVariable int id) {
        return filmService.findFilmById(id);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public Film putLike(@PathVariable("id") int filmId, @PathVariable("userId") int userId) {
        return filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public Film removeLike(@PathVariable("id") int filmId, @PathVariable("userId") int userId) {
        return filmService.removeLike(filmId, userId);
    }

    @GetMapping("/films/popular")
    public List<Film> getBestFilms(@RequestParam(defaultValue = "10") Integer count) {
        return filmService.getBestFilms(count);
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