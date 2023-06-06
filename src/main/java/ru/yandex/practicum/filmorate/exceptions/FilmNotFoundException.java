package ru.yandex.practicum.filmorate.exceptions;

import org.springframework.http.HttpStatus;

public class FilmNotFoundException extends RuntimeException {
    public FilmNotFoundException(String message, HttpStatus ok) {
        super(message);
    }
}