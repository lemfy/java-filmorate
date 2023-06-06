package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Friends;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public User createUser(@NotNull @RequestBody @Valid User user) {
        log.info("Post request {}", user);
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
        log.info("User added {}", user);
        return userService.createUser(user);
    }

    @PutMapping("/users")
    public User changeUser(@NotNull @RequestBody @Valid User user) {
        log.info("Put request {}", user);
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
        log.info("User changed {}", user);
        return userService.changeUser(user.getId(), user);
    }

    @GetMapping("/users")
    public List<User> findAllUsers() {
        return userService.findAllUsers();
    }

    @GetMapping("/users/{id}")
    public User findUserById(@PathVariable int id) {
        return userService.findUserById(id);
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public Friends addFriend(@PathVariable int id, @PathVariable int friendId) {
        return userService.addFriend(id, friendId);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable int id, @PathVariable int friendId) {
        userService.removeFriend(id, friendId);
    }

    @GetMapping("/users/{id}/friends")
    public List<User> findAllFriends(@PathVariable("id") int id) {
        return userService.findAllFriends(id);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<User> findCommonFriends(@PathVariable("id") int firstUserId, @PathVariable("otherId") int secondUserId) {
        return userService.findCommonFriends(firstUserId, secondUserId);
    }
}