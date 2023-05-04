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
        log.debug("List size {}", users.size());
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User create(@NotNull @RequestBody @Valid User user) {
        log.info("Post request {}", user);
        validate(user);
        user.setId(id++);
        users.put(user.getId(), user);
        log.info("User added {}", user);
        return user;
    }

    @PutMapping
    public User change(@NotNull @RequestBody @Valid User user) {
        log.info("Put request {}", user);
        if (users.containsKey(user.getId())) {
            validate(user);
            users.put(user.getId(), user);
            log.info("User changed {}", user);
        } else {
            log.debug("Key not found {}", user.getId());
            throw new ValidationException("Пользователь не найден");
        }
        return user;
    }

    private void validate(User user) {
        if ((user.getEmail().isBlank() || user.getEmail().isEmpty() || !user.getEmail().contains("@"))) {
            log.error("User email is empty or invalid {}", user.getName());
            throw new ValidationException("Имейл не может быть пустым или не содержать символ @");
        }
        if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.error("User login is empty or invalid {}", user.getName());
            throw new ValidationException("Название не может быть пустым или содержать пробелы");
        }
        if (user.getName() == null || user.getName().isEmpty() || user.getName().isBlank()) {
            log.error("User name is empty {}", user.getName());
            user.setName(user.getLogin());
        }
        if (user.getBirthday().equals(LocalDate.now()) || user.getBirthday().isAfter(LocalDate.now())) {
            log.error("User birthday is invalid. {}", user.getName());
            throw new ValidationException("Неверная дата рождения");
        }
    }
}