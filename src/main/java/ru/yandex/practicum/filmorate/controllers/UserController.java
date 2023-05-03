package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private int id = 1;
    public Map<Integer, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> allUsers() {
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody @Valid User user) {
        validate(user);
        user.setId(id++);
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User change(@RequestBody @Valid User user) {
        validate(user);
        users.put(user.getId(), user);
        return user;
    }

    private void validate(User user) {
        if ((user.getEmail().isBlank() || !user.getEmail().contains("@"))) {
            throw new ValidationException("Имейл не может быть пустым или не содержать символ @");
        }
        if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            throw new ValidationException("Название не может быть пустым или содержать пробелы");
        }
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().equals(LocalDate.now()) || user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Неверная дата рождения");
        }
    }
}