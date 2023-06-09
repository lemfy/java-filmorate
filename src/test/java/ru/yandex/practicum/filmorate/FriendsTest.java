package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Friends;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dao.DBFriendsStorage;
import ru.yandex.practicum.filmorate.storage.dao.DBUserStorage;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor__ = @Autowired)
public class FriendsTest {
    final DBUserStorage userStorage;
    final DBFriendsStorage friendsStorage;

    @Test
    void testAddFriends() {
        User user1 = userStorage.createUser(new User(1, "AAA@ya.ru", "AAA", "",
                LocalDate.of(2000, 1, 1)));
        User user2 = userStorage.createUser(new User(2, "BBB@ya.ru", "BBB", "BBB",
                LocalDate.of(2000, 1, 2)));

        friendsStorage.addFriend(new Friends(user1.getId(), user2.getId(), true));

        final List<Friends> friendsForUser1 = friendsStorage.findAllFriends(user1.getId());
        final List<Friends> friendsForUser2 = friendsStorage.findAllFriends(user2.getId());

        assertThat(friendsForUser1)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);
        assertThat(friendsForUser2)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);
    }

    @Test
    void testUpdateFriends() {
        User user1 = userStorage.createUser(new User(1, "AAA@ya.ru", "AAA", "",
                LocalDate.of(2000, 1, 1)));
        User user2 = userStorage.createUser(new User(2, "BBB@ya.ru", "BBB", "BBB",
                LocalDate.of(2000, 1, 2)));

        friendsStorage.addFriend(new Friends(user1.getId(), user2.getId(), true));
        friendsStorage.update(new Friends(user1.getId(), user2.getId(), false));


        final List<Friends> friendsForUser1 = friendsStorage.findAllFriends(user1.getId());
        final List<Friends> friendsForUser2 = friendsStorage.findAllFriends(user2.getId());

        assertThat(friendsForUser1)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);
        assertThat(friendsForUser2)
                .isNotNull()
                .isEmpty();
    }

    @Test
    void testDeleteFriends() {
        User user1 = userStorage.createUser(new User(1, "AAA@ya.ru", "AAA", "",
                LocalDate.of(2000, 1, 1)));
        User user2 = userStorage.createUser(new User(2, "BBB@ya.ru", "BBB", "BBB",
                LocalDate.of(2000, 1, 2)));

        final Friends friendship = friendsStorage.addFriend(new Friends(user1.getId(), user2.getId(), true));

        friendsStorage.deleteFriend(friendship);

        final List<Friends> friendsForUser1 = friendsStorage.findAllFriends(user1.getId());
        final List<Friends> friendsForUser2 = friendsStorage.findAllFriends(user2.getId());

        assertThat(friendsForUser1)
                .isEmpty();
        assertThat(friendsForUser2)
                .isEmpty();
    }

    @Test
    void testFindAllFriends() {
        User user1 = userStorage.createUser(new User(1, "AAA@ya.ru", "AAA", "",
                LocalDate.of(2000, 1, 1)));
        User user2 = userStorage.createUser(new User(2, "BBB@ya.ru", "BBB", "BBB",
                LocalDate.of(2000, 1, 2)));
        User user3 = userStorage.createUser(new User(3, "", "CCC", "CCC",
                LocalDate.of(2000, 1, 2)));

        friendsStorage.addFriend(new Friends(user1.getId(), user2.getId(), true));
        friendsStorage.addFriend(new Friends(user1.getId(), user3.getId(), true));


        final List<Friends> friendsForUser1 = friendsStorage.findAllFriends(user1.getId());
        assertThat(friendsForUser1)
                .isNotNull()
                .isNotEmpty()
                .hasSize(2);
    }
}