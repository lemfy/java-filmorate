package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controllers.UserController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class UserTests {
    UserController userController;

    User user1 = new User(1, "AAA@ya.ru", "AAA", "",
            LocalDate.of(2000, 1, 1), null);
    User user2 = new User(2, "BBB@ya.ru", "BBB", "BBB",
            LocalDate.of(2000, 1, 2), null);
    User user3 = new User(3, "", "CCC", "CCC",
            LocalDate.of(2000, 1, 2), null);
    User user4 = new User(4, "DDD@ya.ru", "", "DDD",
            LocalDate.of(2000, 1, 4), null);
    User user5 = new User(5, "EEE@ya.ru", "E E", "EEE",
            LocalDate.of(2000, 1, 5), null);
    User user6 = new User(6, "FFF@ya.ru", "FFF", "FFF",
            LocalDate.of(2030, 1, 6), null);
    User user7 = new User(7, "GGG@ya.ru", "GGG", "GGG",
            LocalDate.now(), null);

    @Test
    void shouldAddUser() {
        userController.createUser(user1);
        assertTrue(userController.findAllUsers().contains(user1));
    }

    @Test
    void shouldReturnAllUsers() {
        assertEquals(0, userController.findAllUsers().size());
        userController.createUser(user1);
        userController.createUser(user2);
        Collection<User> users = userController.findAllUsers();
        assertEquals(2, users.size());
        assertTrue(userController.findAllUsers().contains(user1));
        assertTrue(userController.findAllUsers().contains(user2));
    }

    @Test
    void shouldNotAddUserWhenEmailIsEmpty() {
        assertThrows(ValidationException.class, () -> userController.createUser(user3));
        assertFalse(userController.findAllUsers().contains(user3));
    }

    @Test
    void shouldNotAddUserWhenEmailIsInvalid() {
        assertThrows(ValidationException.class, () -> userController.createUser(user3));
        assertFalse(userController.findAllUsers().contains(user3));
    }

    @Test
    void shouldNotAddUserWhenLoginIsBlank() {
        assertThrows(ValidationException.class, () -> userController.createUser(user4));
        assertFalse(userController.findAllUsers().contains(user4));
    }

    @Test
    void shouldNotAddUserWhenLoginContainsSpaces() {
        assertThrows(ValidationException.class, () -> userController.createUser(user5));
        assertFalse(userController.findAllUsers().contains(user5));
    }

    @Test
    void shouldGiveNameFromLoginIfNameIsEmpty() {
        userController.createUser(user1);
        assertTrue(userController.findAllUsers().contains(user1));
        assertEquals(user1.getLogin(), user1.getName());
    }

    @Test
    void shouldAddUserWhenBirthdayIsEqualsAfterNow() {
        assertThrows(ValidationException.class, () -> userController.createUser(user6));
        assertFalse(userController.findAllUsers().contains(user6));
    }

    @Test
    void shouldNotAddUserWhenBirthdayIsNow() {
        assertThrows(ValidationException.class, () -> userController.createUser(user7));
        assertFalse(userController.findAllUsers().contains(user7));
    }
}