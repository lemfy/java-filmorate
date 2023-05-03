package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
public class User {
    private Integer id;
    @NotBlank(message = "Имейл не может быть пустым")
    @Email(message = "Невалидный Имейл")
    private String email;
    @NotBlank(message = "Логин не может быть пустым")
    @Pattern(regexp = "\\S+", message = "Не может содержать пробелы")
    private String login;
    private String name;
    @Past
    private LocalDate birthday;

    public User(String email, String login, String name, LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }


    public User(String email, String login, LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.name = login;
        this.birthday = birthday;
    }
}