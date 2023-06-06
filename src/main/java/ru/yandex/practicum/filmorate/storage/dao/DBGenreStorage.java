package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Genres;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.ArrayList;
import java.util.List;

@Component
public class DBGenreStorage extends DbStorage implements GenreStorage {

    public DBGenreStorage(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public Genres findGenreById(int id) {
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet("name from genre where id = ?", id);
        if (rowSet.next()) {
            return mapToRow(rowSet);
        } else {
            throw new FilmNotFoundException("Жанр не найден", HttpStatus.OK);
        }
    }

    @Override
    public List<Genres> getAllGenres() {
        List<Genres> genres = new ArrayList<>();
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select id");
        while (rowSet.next()) {
            Genres genre = mapToRow(rowSet);
            genres.add(genre);
        }
        return genres;
    }

    private Genres mapToRow(SqlRowSet rowSet) {
        int id = rowSet.getInt("id");
        String name = rowSet.getString("name");
        return Genres.builder()
                .id(id)
                .name(name)
                .build();
    }
}