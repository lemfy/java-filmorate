package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private int id = 1;
    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public User createUser(User user) {
        user.setId(id++);
        user.setFriends(new HashSet<>());
        users.put(user.getId(), user);
        log.info("user added");
        return users.get(user.getId());
    }

    @Override
    public User changeUser(int id, User user) {
        if (!users.containsKey(id)) {
            throw new UserNotFoundException(String.format("Пользователя с id %d не существует.", id));
        }
        Set<Integer> friends = users.get(id).getFriends();
        users.put(user.getId(), user);
        user.setFriends(friends);
        log.info("user changed");
        return users.get(user.getId());
    }

    @Override
    public List<User> findAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User findUserById(int id) {
        if (!users.containsKey(id)) {
            throw new UserNotFoundException(String.format("Пользователя с id %d не существует.", id));
        }
        log.info("currently user found");
        return users.get(id);
    }

    @Override
    public User addFriend(int userId, int friendId) {
        if (!users.containsKey(userId)) {
            throw new UserNotFoundException(String.format("Пользователя с id %d не существует.", userId));
        }
        if (!users.containsKey(friendId)) {
            throw new UserNotFoundException(String.format("Пользователя с id %d не существует.", friendId));
        }

        users.get(userId).getFriends().add(friendId);
        users.get(friendId).getFriends().add(userId);
        log.info("Пользователь с id: {} добавил в друзья пользователя с id: {}", userId, friendId);
        return users.get(userId);
    }

    @Override
    public User removeFriend(int userId, int friendId) {
        if (!users.containsKey(userId)) {
            throw new UserNotFoundException(String.format("Пользователя с id %d не существует.", userId));
        }
        if (!users.containsKey(friendId)) {
            throw new UserNotFoundException(String.format("Пользователя с id %d не существует.", friendId));
        }

        users.get(userId).getFriends().remove(friendId);
        users.get(friendId).getFriends().remove(userId);
        log.info("Пользователь с id: {} удалил из друзей пользователя с id: {}", userId, friendId);
        return users.get(userId);
    }

    @Override
    public List<User> findAllFriends(int id) {
        if (!users.containsKey(id)) {
            throw new UserNotFoundException(String.format("Пользователя с id %d не существует.", id));
        }
        Set<Integer> userFriends = users.get(id).getFriends();
        log.info("all friends found");
        return userFriends.stream()
                .map(users::get)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> findCommonFriends(int firstUserId, int secondUserId) {
        if (!users.containsKey(firstUserId)) {
            throw new UserNotFoundException(String.format("Пользователя с id %d не существует.", firstUserId));
        }
        if (!users.containsKey(secondUserId)) {
            throw new UserNotFoundException(String.format("Пользователя с id %d не существует.", secondUserId));
        }
        Set<Integer> firstUserFriends = users.get(firstUserId).getFriends();
        Set<Integer> secondUserFriends = users.get(secondUserId).getFriends();
        log.info("common friends found");
        return firstUserFriends.stream()
                .filter(secondUserFriends::contains)
                .map(users::get)
                .collect(Collectors.toList());
    }
}