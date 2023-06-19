package ru.yandex.practicum.filmorate.storage.dao;

import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.util.*;

@Repository("DBFilmStorage")
@Primary
public class DBFilmStorage extends DbStorage implements FilmStorage {
    private final Logger log = (Logger) LoggerFactory.getLogger(DBFilmStorage.class);

    public DBFilmStorage(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public Film createFilm(Film film) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("Films")
                .usingGeneratedKeyColumns("id");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", film.getName());
        parameters.put("description", film.getDescription());
        parameters.put("releaseDate", film.getReleaseDate());
        parameters.put("duration", film.getDuration());
        parameters.put("MpaId", film.getMpa().getId());

        Number userKey = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));

        film.setId(userKey.intValue());
        return film;
    }

    @Override
    public Film changeFilm(Film film) {
        String updateSql = "update Films set name = ?, description = ?, releaseDate = ?, duration = ?, MpaId = ? where id = ?";
        if (jdbcTemplate.update(updateSql,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId())
                <= 0) {
            log.error("Фильм не найден {}", film.getId());
            throw new FilmNotFoundException("Фильм не найден");
        } else {
            return film;
        }
    }

    @Override
    public Film findFilmById(int id) {
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("select id, name, description, releaseDate, duration, MpaId from Films where id = ?", id);
        if (sqlRowSet.next()) {
            return mapToRow(sqlRowSet);
        } else {
            throw new FilmNotFoundException(String.format("Film's id %d doesn't found!", id));
        }
    }

    @Override
    public List<Film> findAllFilms() {
        List<Film> films = new ArrayList<>();
        String sql = "select id, name, description, releaseDate, duration, MpaId  from Films";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql);
        while (rowSet.next()) {
            Film film = mapToRow(rowSet);
            films.add(film);
        }
        log.info("Количество фильмов: {}", films.size());
        return films;
    }

    private Film mapToRow(SqlRowSet sqlRowSet) {
        int mpaId = sqlRowSet.getInt("MpaId");
        int id = sqlRowSet.getInt("id");
        String name = sqlRowSet.getString("name");
        String description = sqlRowSet.getString("description");
        LocalDate date = Objects.requireNonNull(sqlRowSet.getDate("releaseDate")).toLocalDate();
        int duration = sqlRowSet.getInt("duration");
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