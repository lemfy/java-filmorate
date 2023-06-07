package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@Builder
public class User {
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