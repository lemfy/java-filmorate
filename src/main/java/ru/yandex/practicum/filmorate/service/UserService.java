package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Friends;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.FriendsStorage;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final UserStorage userStorage;
    private final FriendsStorage friendsStorage;

    @Autowired
    public UserService(UserStorage userStorage, FriendsStorage friendsStorage) {
        this.userStorage = userStorage;
        this.friendsStorage = friendsStorage;
    }

    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    public User changeUser(int id, User user) {
        return userStorage.changeUser(id, user);
    }

    public List<User> findAllUsers() {
        return userStorage.findAllUsers();
    }

    public User findUserById(int id) {
        return userStorage.findUserById(id);
    }

    public Friends addFriend(int userId, int friendId) {
        var friends = friendsStorage.findCommonFriends(userId, friendId);
        if (friends == null) {
            try {
                return friendsStorage.addFriend(new Friends(userId, friendId, false));
            } catch (Exception e) {
                throw new UserNotFoundException("Нет данных о ждужбе");
            }
        } else if (friends.isStatus()) {
            return friends;
        } else if (friends.getFriendId() == userId) {
            friends.setStatus(true);
            return friendsStorage.update(friends);
        }
        return friends;
    }

    public void removeFriend(int userId, int friendId) {
        var friends = friendsStorage.findCommonFriends(userId, friendId);
        if (friends == null) {
            throw new UserNotFoundException("Нет данных о ждужбе");
        } else {
            if (friends.isStatus()) {
                if (friends.getUserId() == userId) {
                    friends.setUserId(friendId);
                    friends.setFriendId(userId);
                }
                friends.setStatus(false);
                friendsStorage.update(friends);
            } else {
                if (friends.getUserId() == userId) {
                    friendsStorage.deleteFriend(friends);
                }
            }
        }
    }

    public List<User> findAllFriends(int userId) {
        var friends = friendsStorage.findAllFriends(userId);
        List<User> users = new ArrayList<>();
        for (var friend : friends) {
            int id;
            if (friend.getUserId() == userId)
                id = friend.getFriendId();
            else
                id = friend.getUserId();
            var user = userStorage.findUserById(id);
            users.add(user);
        }
        return users;
    }

    public List<User> findCommonFriends(int userId, int otherUserId) {
        var friendshipsByUser1 = friendsStorage.findAllFriends(userId);
        var friendshipsByUser2 = friendsStorage.findAllFriends(otherUserId);
        List<Integer> friendIdByUser1 = new ArrayList<>();
        List<Integer> friendIdByUser2 = new ArrayList<>();
        for (var friendshipsByUser : friendshipsByUser1) {
            int id;
            if (friendshipsByUser.getUserId() == userId)
                id = friendshipsByUser.getFriendId();
            else
                id = friendshipsByUser.getUserId();
            friendIdByUser1.add(id);
        }
        for (var friendshipsByUser : friendshipsByUser2) {
            int id;
            if (friendshipsByUser.getUserId() == otherUserId)
                id = friendshipsByUser.getFriendId();
            else
                id = friendshipsByUser.getUserId();
            friendIdByUser2.add(id);
        }
        friendIdByUser1.retainAll(friendIdByUser2);
        List<User> users = new ArrayList<>();

        for (var commonFriendId : friendIdByUser1) {
            users.add(userStorage.findUserById(commonFriendId));
        }
        return users;
    }
}