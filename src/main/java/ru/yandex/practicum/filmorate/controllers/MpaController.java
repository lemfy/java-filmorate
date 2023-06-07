package ru.yandex.practicum.filmorate.controllers;

import ch.qos.logback.classic.Logger;
import lombok.AllArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RestController
@RequestMapping("/mpa")
@AllArgsConstructor
public class MpaController {
    private final Logger log = (Logger) LoggerFactory.getLogger(MpaController.class);
    private final FilmService filmService;

    @GetMapping
    public List<Mpa> getMpaList() {
        log.info("запрос списка рейтингов");
        return filmService.getMpaList();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mpa getMpa(@PathVariable int id) {
        log.info("запрос рейтинга по id");
        return filmService.getMpa(id);
    }
}