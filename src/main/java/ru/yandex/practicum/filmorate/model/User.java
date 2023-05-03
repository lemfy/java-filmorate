package ru.yandex.practicum.filmorate.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class User {
    @Min(value = 0)
    private int id;
    @NotBlank(message = "Имейл не может быть пустым")
    @Email(message = "Невалидный Имейл")
    private String email;
    @NotBlank(message = "Логин не может быть пустым")
    @Pattern(regexp = "\\S+", message = "Не может содержать пробелы")
    private String login;
    private String name;
    @Past
    private LocalDate birthday;
}