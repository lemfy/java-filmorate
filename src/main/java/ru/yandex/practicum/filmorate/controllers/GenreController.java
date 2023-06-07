package ru.yandex.practicum.filmorate.controllers;

import ch.qos.logback.classic.Logger;
import lombok.AllArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Genres;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RestController
@RequestMapping("/genres")
@AllArgsConstructor
public class GenreController {
    private final Logger log = (Logger) LoggerFactory.getLogger(GenreController.class);
    private final FilmService filmService;

    @GetMapping
    public List<Genres> getGenreList() {
        log.info("запрос списка жанров");
        return filmService.getGenresList();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Genres getGenre(@PathVariable int id) {
        log.info("запрос жанра по id");
        return filmService.getGenres(id);
    }
}