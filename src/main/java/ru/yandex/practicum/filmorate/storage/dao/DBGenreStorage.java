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
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("name from genre where id = ?", id);
        if (sqlRowSet.next()) {
            return mapToRow(sqlRowSet);
        } else {
            throw new FilmNotFoundException("Жанр не найден");
        }
    }

    @Override
    public List<Genres> getAllGenres() {
        List<Genres> genres = new ArrayList<>();
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("select id");
        while (sqlRowSet.next()) {
            Genres genre = mapToRow(sqlRowSet);
            genres.add(genre);
        }
        return genres;
    }

    private Genres mapToRow(SqlRowSet sqlRowSet) {
        int id = sqlRowSet.getInt("id");
        String name = sqlRowSet.getString("name");
        return Genres.builder()
                .id(id)
                .name(name)
                .build();
    }
}