package ru.yandex.practicum.filmorate.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Genres;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RestController
@RequestMapping("/genres")
@AllArgsConstructor
public class GenreController {
    private final FilmService filmService;

    @GetMapping
    public List<Genres> getGenreList() {
        return filmService.getGenresList();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Genres getGenre(@PathVariable int id) {
        return filmService.getGenres(id);
    }
}