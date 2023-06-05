package ru.yandex.practicum.filmorate.storage.dao;

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
        SqlRowSet RowSet = jdbcTemplate.queryForRowSet("name from genre where id = ?", id);
        if (RowSet.next()) {
            return mapToRow(RowSet);
        } else {
            throw new FilmNotFoundException("Жанр не найден");
        }
    }

    @Override
    public List<Genres> getAllGenres() {
        List<Genres> genres = new ArrayList<>();
        SqlRowSet RowSet = jdbcTemplate.queryForRowSet("select id");
        while (RowSet.next()) {
            Genres genre = mapToRow(RowSet);
            genres.add(genre);
        }
        return genres;
    }

    private Genres mapToRow(SqlRowSet RowSet) {
        int id = RowSet.getInt("id");
        String name = RowSet.getString("name");
        return Genres.builder()
                .id(id)
                .name(name)
                .build();
    }
}