package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controllers.UserController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class UserTests {
    UserController userController = new UserController();

    User user1 = new User("AAA@ya.ru", "AAA", "",
            LocalDate.of(2000, 1, 1));
    User user2 = new User("BBB@ya.ru", "BBB", "BBB",
            LocalDate.of(2000, 1, 2));
    User user3 = new User("", "CCC", "CCC",
            LocalDate.of(2000, 1, 2));
    User user4 = new User("DDD@ya.ru", "", "DDD",
            LocalDate.of(2000, 1, 4));
    User user5 = new User("EEE@ya.ru", "E E", "EEE",
            LocalDate.of(2000, 1, 5));
    User user6 = new User("FFF@ya.ru", "FFF", "FFF",
            LocalDate.of(2030, 1, 6));
    User user7 = new User("GGG@ya.ru", "GGG", "GGG",
            LocalDate.now());

    @Test
    void shouldAddUser() {
        userController.create(user1);
        assertTrue(userController.allUsers().contains(user1));
    }

    @Test
    void shouldReturnAllUsers() {
        assertEquals(0, userController.allUsers().size());
        userController.create(user1);
        userController.create(user2);
        Collection<User> users = userController.allUsers();
        assertEquals(2, users.size());
        assertTrue(userController.allUsers().contains(user1));
        assertTrue(userController.allUsers().contains(user2));
    }

    @Test
    void shouldNotAddUserWhenEmailIsEmpty() {
        assertThrows(ValidationException.class, () -> userController.create(user3));
        assertFalse(userController.allUsers().contains(user3));
    }

    @Test
    void shouldNotAddUserWhenEmailIsInvalid() {
        assertThrows(ValidationException.class, () -> userController.create(user3));
        assertFalse(userController.allUsers().contains(user3));
    }

    @Test
    void shouldNotAddUserWhenLoginIsBlank() {
        assertThrows(ValidationException.class, () -> userController.create(user4));
        assertFalse(userController.allUsers().contains(user4));
    }

    @Test
    void shouldNotAddUserWhenLoginContainsSpaces() {
        assertThrows(ValidationException.class, () -> userController.create(user5));
        assertFalse(userController.allUsers().contains(user5));
    }

    @Test
    void shouldGiveNameFromLoginIfNameIsEmpty() {
        userController.create(user1);
        assertTrue(userController.allUsers().contains(user1));
        assertEquals(user1.getLogin(), user1.getName());
    }

    @Test
    void shouldAddUserWhenBirthdayIsEqualsAfterNow() {
        assertThrows(ValidationException.class, () -> userController.create(user6));
        assertFalse(userController.allUsers().contains(user6));
    }

    @Test
    void shouldNotAddUserWhenBirthdayIsNow() {
        assertThrows(ValidationException.class, () -> userController.create(user7));
        assertFalse(userController.allUsers().contains(user7));
    }
}