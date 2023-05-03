package ru.yandex.practicum.filmorate.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class Film {
    @Min(value = 0)
    private Integer id;
    @NotBlank(message = "Имя не может быть пустым")
    private String name;
    @NotNull
    @Size(max = 200)
    private String description;
    @NotNull
    private LocalDate releaseDate;
    @NotNull
    @Positive
    private Integer durations;
}