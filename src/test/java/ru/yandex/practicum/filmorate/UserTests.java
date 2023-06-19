package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dao.DBUserStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor__ = @Autowired)
public class UserTests {
    final DBUserStorage userStorage;

    @Test
    void testCreateUser() {
        final User user111 = userStorage.createUser(new User(1, "AAA@ya.ru", "AAA", "",
                LocalDate.of(2000, 1, 1)));

        assertThat(user111).isNotNull();
        assertThat(user111.getId()).isNotZero();
    }

    @Test
    void testChangeUser() {
        userStorage.createUser(new User(1, "AAA@ya.ru", "AAA", "",
                LocalDate.of(2000, 1, 1)));
        new User(2, "BBB@ya.ru", "BBB", "BBB",
                LocalDate.of(2000, 1, 2));
        final User user111 = userStorage.changeUser(2, new User(3, "", "CCC", "CCC",
                LocalDate.of(2000, 1, 2)));

        assertThat(user111).isNotNull();
        assertThat(user111.getId()).isNotZero();
    }

    @Test
    void testFindUserById() {
        User user111 = userStorage.createUser(new User(3, "", "CCC", "CCC",
                LocalDate.of(2000, 1, 2)));
        final Optional<User> userOptional = Optional.ofNullable(userStorage.findUserById(user111.getId()));
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", user111.getId()));
    }

    @Test
    void testFindAllUsers() {
        User user4 = new User(4, "DDD@ya.ru", "", "DDD",
                LocalDate.of(2000, 1, 4));
        User user5 = new User(5, "EEE@ya.ru", "E E", "EEE",
                LocalDate.of(2000, 1, 5));
        userStorage.createUser(user4);
        userStorage.createUser(user5);

        final List<User> users = userStorage.findAllUsers();

        assertThat(users)
                .isNotNull()
                .isNotEmpty()
                .contains(user4, user5);
    }
}