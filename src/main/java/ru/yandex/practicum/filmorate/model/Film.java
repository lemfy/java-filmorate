package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
public class Film {
    private int id;
    @NotNull
    @NotBlank(message = "Имя не может быть пустым")
    private String name;
    @NotNull
    @Size(max = 200)
    private String description;
    @NotNull
    private LocalDate releaseDate;
    @NotNull
    @Positive
    private int durations;

    public Film(String name, String description, LocalDate releaseDate, int durations) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.durations = durations;
    }
}