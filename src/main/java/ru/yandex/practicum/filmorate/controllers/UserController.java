package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private int id = 1;
    public Map<Integer, User> users = new HashMap<>();

    @GetMapping
    public List<User> allUsers() {
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User create(@NotNull @RequestBody @Valid User user) {
        validate(user);
        user.setId(id++);
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User change(@NotNull @RequestBody @Valid User user) {
        if (users.containsKey(user.getId())) {
            validate(user);
            users.put(user.getId(), user);
        } else {
            throw new ValidationException("Пользователь не найден");
        }
        return user;
    }

    private void validate(User user) {
        if ((user.getEmail().isBlank() || user.getEmail().isEmpty() || !user.getEmail().contains("@"))) {
            throw new ValidationException("Имейл не может быть пустым или не содержать символ @");
        }
        if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            throw new ValidationException("Название не может быть пустым или содержать пробелы");
        }
        if (user.getName() == null || user.getName().isEmpty() || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().equals(LocalDate.now()) || user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Неверная дата рождения");
        }
    }
}