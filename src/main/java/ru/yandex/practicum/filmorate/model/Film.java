package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
@Builder
public class Film {
    @Min(value = 0)
    private int id;
    @NotBlank(message = "Имя не может быть пустым")
    private String name;
    @Size(max = 200)
    private String description;
    private LocalDate releaseDate;
    @Positive
    private int duration;
    private Set<Integer> likes;
    private List<Genres> genres;
    @NotNull
    private Mpa mpa;

    public final int getLikeSize() {
        if (likes == null) {
            return 0;
        }
        return likes.size();
    }
}