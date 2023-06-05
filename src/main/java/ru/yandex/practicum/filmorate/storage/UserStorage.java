package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    User createUser(User user);

    User changeUser(int id, User user);

    List<User> findAllUsers();

    User findUserById(int id);

    User addFriend(int userId, int friendId);

    User removeFriend(int userId, int friendId);

    List<User> findAllFriends(int userId);

    List<User> findCommonFriends(int userId, int otherUserId);
}