package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.HashSet;
import java.util.Set;

@Component
public class DBMpaStorage extends DbStorage implements MpaStorage {

    public DBMpaStorage(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public Mpa getMpaById(int id) {
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet("name from Mpa where id = ?", id);
        if (rowSet.next()) {
            return mapToRow(rowSet);
        } else {
            throw new FilmNotFoundException("Рейтинг не найден");
        }
    }

    @Override
    public Set<Mpa> getAllMpa() {
        Set<Mpa> mpaList = new HashSet<>();
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select id");
        while (rowSet.next()) {
            Mpa mpa = mapToRow(rowSet);
            mpaList.add(mpa);
        }
        return mpaList;
    }

    private Mpa mapToRow(SqlRowSet rowSet) {
        int id = rowSet.getInt("id");
        String name = rowSet.getString("name");
        return Mpa.builder()
                .id(id)
                .name(name)
                .build();
    }
}