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
@AllArgsConstructor
public class FilmController {

    private final FilmService filmService;

    @GetMapping("/films")
    @ResponseStatus(HttpStatus.OK)
    public List<Film> findAllFilms() {
        return filmService.findAllFilms();
    }

    @PostMapping("/films")
    public Film createFilm(@RequestBody @Valid Film film) {
        log.info("Post request {}", film);
        //    validate(film);
        log.info("Film added {}", film);
        return filmService.createFilm(film);
    }

    @PutMapping("/films")
    @ResponseStatus(HttpStatus.OK)
    public Film changeFilm(@RequestBody @Valid Film film) {
        log.info("Put request: {}", film);
        //    validate(film);
        log.info("Film changed {}", film);
        return filmService.changeFilm(film.getId(), film);
    }

    @GetMapping("/films/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Film findFilmById(@PathVariable int id) {
        return filmService.findFilmById(id);
    }

    @PutMapping("/films/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void putLike(@PathVariable("id") int filmId, @PathVariable("userId") int userId) {
        filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void removeLike(@PathVariable("id") int filmId, @PathVariable("userId") int userId) {
        filmService.removeLike(filmId, userId);
    }

    @GetMapping("/films/popular")
    @ResponseStatus(HttpStatus.OK)
    public List<Film> getBestFilms(@RequestParam(defaultValue = "10") Integer count) {
        return filmService.getBestFilms(count);
    }
}