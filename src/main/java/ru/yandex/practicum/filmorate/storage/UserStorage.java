package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    User createUser(User user);

    User changeUser(int id, User user);

    List<User> findAllUsers();

    User findUserById(int id);
}