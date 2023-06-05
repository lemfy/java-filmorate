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
        SqlRowSet RowSet = jdbcTemplate.queryForRowSet("name from Mpa where id = ?", id);
        if (RowSet.next()) {
            return mapToRow(RowSet);
        } else {
            throw new FilmNotFoundException("Рейтинг не найден");
        }
    }

    @Override
    public Set<Mpa> getAllMpa() {
        Set<Mpa> MpaList = new HashSet<>();
        SqlRowSet RowSet = jdbcTemplate.queryForRowSet("select id");
        while (RowSet.next()) {
            Mpa Mpa = mapToRow(RowSet);
            MpaList.add(Mpa);
        }
        return MpaList;
    }

    private Mpa mapToRow(SqlRowSet RowSet) {
        int id = RowSet.getInt("id");
        String name = RowSet.getString("name");
        return Mpa.builder()
                .id(id)
                .name(name)
                .build();
    }
}