package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Likes;

import java.util.Set;

public interface LikesStorage {
    void addLike(Likes likes);

    void removeLike(Likes likes);

    Set<Likes> getAllLikes();

    Set<Likes> getLikesWithFilmId(int filmId);

    Likes getLikesCurrentUserWithFilmId(int userId, int filmId);
}