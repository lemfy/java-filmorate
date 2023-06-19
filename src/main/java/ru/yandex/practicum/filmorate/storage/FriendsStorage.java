package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Friends;

import java.util.List;

public interface FriendsStorage {
    Friends addFriend(Friends friend);

    boolean deleteFriend(Friends friend);

    Friends update(Friends friend);

    List<Friends> findAllFriends(int userId);

    Friends findCommonFriends(int userId, int otherUserId);
}