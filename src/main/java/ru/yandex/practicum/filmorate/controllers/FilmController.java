package ru.yandex.practicum.filmorate.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
@AllArgsConstructor
public class FilmController {

    private final FilmService filmService;

    @PostMapping
    public Film createFilm(@RequestBody @Valid Film film) {
        log.info("Film added {}", film);
        return filmService.createFilm(film);
    }

    @PutMapping
    public Film changeFilm(@RequestBody @Valid Film film) {
        log.info("Film changed {}", film);
        return filmService.changeFilm(film);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Film findFilmById(@PathVariable int id) {
        log.info("Запрос фильма с id: {}", id);
        return filmService.findFilmById(id);
    }

    @GetMapping
    public List<Film> findAllFilms() {
        log.info("Запрос всех фильмов");
        return filmService.findAllFilms();
    }

    @PutMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void putLike(@PathVariable int id, @PathVariable int userId) {
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void removeLike(@PathVariable int id, @PathVariable int userId) {
        filmService.removeLike(id, userId);
    }

    @GetMapping("/popular")
    @ResponseStatus(HttpStatus.OK)
    public List<Film> getBestFilms(@RequestParam(defaultValue = "10") int count) {
        return filmService.getBestFilms(count);
    }
}