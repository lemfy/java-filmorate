package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final int id = 1;
    public Map<Integer, User> users = new HashMap<>();

    @Override
    public User createUser(User user) {
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
        if (!users.containsKey(id)) {
            throw new UserNotFoundException(String.format("Пользователя с id %d не существует.", id));
        }
        if (!users.containsKey(friendId)) {
            throw new UserNotFoundException(String.format("Пользователя с id %d не существует.", friendId));
        }
        User firstUser = users.get(userId);
        firstUser.getFriends().add(friendId);
        users.put(firstUser.getId(), firstUser);

        User secondUser = users.get(friendId);
        secondUser.getFriends().add(userId);
        users.put(secondUser.getId(), secondUser);
        return firstUser;
    }

    @Override
    public User removeFriend(int userId, int friendId) {
        if (!users.containsKey(id)) {
            throw new UserNotFoundException(String.format("Пользователя с id %d не существует.", id));
        }
        if (!users.containsKey(friendId)) {
            throw new UserNotFoundException(String.format("Пользователя с id %d не существует.", friendId));
        }
        User firstUser = users.get(userId);
        firstUser.getFriends().remove(friendId);
        users.put(firstUser.getId(), firstUser);

        User secondUser = users.get(friendId);
        secondUser.getFriends().remove(userId);
        users.put(secondUser.getId(), secondUser);
        return firstUser;
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