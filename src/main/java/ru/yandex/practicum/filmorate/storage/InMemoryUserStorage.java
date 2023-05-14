package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private int id = 1;
    public Map<Integer, User> users = new HashMap<>();

    @Override
    public User createUser(User user) {
        user.setId(id++);
        user.setFriends(new HashSet<>());
        users.put(user.getId(), user);
        return users.get(user.getId());
    }

    @Override
    public User changeUser(int id, User user) {
        if (!users.containsKey(id)) {
            throw new UserNotFoundException(String.format("Пользователя с id %d не существует.", id));
        }
        users.put(user.getId(), user);
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
        log.info("Пользователь с id {} добавил в кореша пользователя с id {}", userId, friendId);
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
        log.info("Пользователь с id {} удалил из корешей пользователя с id {}", userId, friendId);
        return users.get(userId);
    }

    @Override
    public List<User> findAllFriends(int id) {
        if (!users.containsKey(id)) {
            throw new UserNotFoundException(String.format("Пользователя с id %d не существует.", id));
        }
        User user = users.get(id);
        List<Integer> friendListId = new ArrayList<>(user.getFriends());
        List<User> friendList = new ArrayList<>();
        for (Integer friends : friendListId) {
            friendList.add(users.get(friends));
        }
        return friendList;
    }

    @Override
    public List<User> findCommonFriends(int firstUserId, int secondUserId) {
        if (!users.containsKey(firstUserId)) {
            throw new UserNotFoundException(String.format("Пользователя с id %d не существует.", firstUserId));
        }
        if (!users.containsKey(secondUserId)) {
            throw new UserNotFoundException(String.format("Пользователя с id %d не существует.", secondUserId));
        }
        User firstUser = users.get(firstUserId);
        List<Integer> firstUserFriendListId = new ArrayList<>(firstUser.getFriends());
        User secondUser = users.get(secondUserId);
        List<Integer> secondUserFriendListId = new ArrayList<>(secondUser.getFriends());

        List<User> commonFriends = new ArrayList<>();
        for (Integer friendId : firstUserFriendListId) {
            if (secondUserFriendListId.contains(friendId)) {
                commonFriends.add(users.get(friendId));
            }
        }
        return commonFriends;
    }
}