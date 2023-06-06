package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Mpa implements Comparable<Mpa> {
    private int id;
    private String name;

    @Override
    public int compareTo(Mpa o) {
        return Integer.compare(this.id, o.id);
    }
}