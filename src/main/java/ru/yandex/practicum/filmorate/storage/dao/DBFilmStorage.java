package ru.yandex.practicum.filmorate.storage.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.LikesStorage;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("DBFilmStorage")
public class DBFilmStorage extends DbStorage implements FilmStorage {

    public DBFilmStorage(JdbcTemplate jdbcTemplate, MpaStorage MpaStorage,
                         GenreStorage genreStorage, LikesStorage likesStorage) {
        super(jdbcTemplate);
    }

    @Override
    public Film createFilm(Film film) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("Films")
                .usingGeneratedKeyColumns("id");

        Map<String, Object> obj = new HashMap<>();
        obj.put("Name", film.getName());
        obj.put("Description", film.getDescription());
        obj.put("ReleaseDate", film.getReleaseDate());
        obj.put("Duration", film.getDuration());
        obj.put("MpaID", film.getMpa().getId());

        Number userKey = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(obj));

        film.setId(userKey.intValue());
        return film;
    }

    @Override
    public Film changeFilm(int id, Film film) {
        String updateSql = "update Films set Name = ?, Description = ?, ReleaseDate = ?, Duration = ?, MpaID = ? where id = ?";
        if (jdbcTemplate.update(updateSql,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId())
                <= 0) {
            throw new FilmNotFoundException("Фильм не найден");
        } else {
            return film;
        }
    }

    @Override
    public Film findFilmById(int id) {
        SqlRowSet RowSet = jdbcTemplate.queryForRowSet("select id, Name, Description, ReleaseDate, Duration, MpaID from Films where id = ?", id);
        if (RowSet.next()) {
            return mapToRow(RowSet);
        } else {
            throw new FilmNotFoundException(String.format("Фильм c id %d не найден", id));
        }
    }

    @Override
    public List<Film> findAllFilms() {
        List<Film> films = new ArrayList<>();
        String sql = "select id, Name, Description, ReleaseDate, Duration, MpaID from Films";
        SqlRowSet RowSet = jdbcTemplate.queryForRowSet(sql);
        while (RowSet.next()) {
            Film film = mapToRow(RowSet);
            films.add(film);
        }
        return films;
    }

    private Film mapToRow(SqlRowSet RowSet) {
        int mpaId = RowSet.getInt("MpaId");
        int id = RowSet.getInt("id");
        String name = RowSet.getString("Name");
        String description = RowSet.getString("Description");
        LocalDate date = RowSet.getDate("ReleaseDate").toLocalDate();
        int duration = RowSet.getInt("Duration");
        Mpa mpa = Mpa.builder()
                .id(mpaId)
                .build();

        return Film.builder()
                .id(id)
                .name(name)
                .description(description)
                .releaseDate(date)
                .duration(duration)
                .mpa(mpa)
                .build();
    }
}