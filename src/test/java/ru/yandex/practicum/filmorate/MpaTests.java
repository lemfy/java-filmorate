package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.dao.DBMpaStorage;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MpaTests {
    private final DBMpaStorage mpaStorage;

    @Test
    public void testGetMpaById() {
        FilmNotFoundException exception = assertThrows(FilmNotFoundException.class, () -> mpaStorage.getMpaById(6));
        assertEquals("not found mpa", exception.getMessage());

        Mpa pg13Rating = new Mpa(1, null);
        pg13Rating.setId(3);
        pg13Rating.setName("PG-13");

        assertEquals(pg13Rating, mpaStorage.getMpaById(3));
    }

    @Test
    public void testGetAllMpa() {
        Set<Mpa> mpaRatings = mpaStorage.getAllMpa();
        assertEquals(5, mpaRatings.size());

        Mpa testRating = new Mpa(1, null);

        testRating.setId(1);
        testRating.setName("G");
        assertTrue(mpaRatings.contains(testRating));

        testRating.setId(2);
        testRating.setName("PG");
        assertTrue(mpaRatings.contains(testRating));

        testRating.setId(3);
        testRating.setName("PG-13");
        assertTrue(mpaRatings.contains(testRating));

        testRating.setId(4);
        testRating.setName("R");
        assertTrue(mpaRatings.contains(testRating));

        testRating.setId(5);
        testRating.setName("NC-17");
        assertTrue(mpaRatings.contains(testRating));
    }
}