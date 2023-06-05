package ru.yandex.practicum.filmorate.storage.dao;

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
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("select id, Name, Description, ReleaseDate, Duration, MpaID from Films where id = ?", id);
        if (sqlRowSet.next()) {
            return mapToRow(sqlRowSet);
        } else {
            throw new FilmNotFoundException(String.format("Фильм c id %d не найден", id));
        }
    }

    @Override
    public List<Film> findAllFilms() {
        List<Film> films = new ArrayList<>();
        String sql = "select id, Name, Description, ReleaseDate, Duration, MpaID from Films";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql);
        while (sqlRowSet.next()) {
            Film film = mapToRow(sqlRowSet);
            films.add(film);
        }
        return films;
    }

    private Film mapToRow(SqlRowSet sqlRowSet) {
        int mpaId = sqlRowSet.getInt("MpaId");
        int id = sqlRowSet.getInt("id");
        String name = sqlRowSet.getString("Name");
        String description = sqlRowSet.getString("Description");
        LocalDate date = sqlRowSet.getDate("ReleaseDate").toLocalDate();
        int duration = sqlRowSet.getInt("Duration");
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