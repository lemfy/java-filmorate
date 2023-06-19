package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Genres;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor__ = @Autowired)

public class GenresTests {

    final GenreStorage genreStorage;


    @Test
    void testFindGenreById() {
        FilmNotFoundException exception = assertThrows(FilmNotFoundException.class, () -> genreStorage.findGenreById(7));
        assertEquals("Жанр не найден", exception.getMessage());

        Genres documentaryGenre = new Genres();
        documentaryGenre.setId(5);
        documentaryGenre.setName("Документальный");
        assertEquals(documentaryGenre, genreStorage.findGenreById(5));
    }

    @Test
    void testGetAllGenres() {
        List<Genres> genres = genreStorage.getAllGenres();
        assertEquals(6, genres.size());

        Genres testGenre = new Genres();

        testGenre.setId(1);
        testGenre.setName("Комедия");
        assertTrue(genres.contains(testGenre));

        testGenre.setId(2);
        testGenre.setName("Драма");
        assertTrue(genres.contains(testGenre));

        testGenre.setId(3);
        testGenre.setName("Мультфильм");
        assertTrue(genres.contains(testGenre));

        testGenre.setId(4);
        testGenre.setName("Триллер");
        assertTrue(genres.contains(testGenre));

        testGenre.setId(5);
        testGenre.setName("Документальный");
        assertTrue(genres.contains(testGenre));

        testGenre.setId(6);
        testGenre.setName("Боевик");
        assertTrue(genres.contains(testGenre));
    }
}