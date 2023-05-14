package ru.yandex.practicum.filmorate.model;

import java.util.Comparator;

public class ComparatorOfPopularity implements Comparator<Film> {
    @Override
    public int compare(Film o1, Film o2) {
/* Если положительный результат => у второго фильма больше лайков
        Если ноль => равное количество лайков
        Если отрицательный результат => у второго фильма меньше лайков */
        return Integer.compare(o1.getLikes().size(), o2.getLikes().size());
    }
}