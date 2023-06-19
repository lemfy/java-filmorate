package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.storage.FilmGenreStorage;

import java.util.ArrayList;
import java.util.List;

@Repository
public class DBFilmGenreStorage extends DbStorage implements FilmGenreStorage {

    public DBFilmGenreStorage(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public List<FilmGenre> getAllFilmGenre() {
        List<FilmGenre> filmGenres = new ArrayList<>();
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("select FilmID, GenreID from FilmGenre");
        while (sqlRowSet.next()) {
            FilmGenre filmGenre = mapToRow(sqlRowSet);
            filmGenres.add(filmGenre);
        }
        return filmGenres;
    }

    @Override
    public List<FilmGenre> getLikesFilmId(int filmId) {
        List<FilmGenre> filmGenres = new ArrayList<>();
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select FilmID, GenreID from FilmGenre where FilmID = ?", filmId);
        while (rowSet.next()) {
            var filmGenre = mapToRow(rowSet);
            filmGenres.add(filmGenre);
        }
        return filmGenres;
    }

    @Override
    public FilmGenre add(FilmGenre filmGenre) {

        String sql = "insert into FilmGenre (FilmID, GenreID) values(?, ?)";
        jdbcTemplate.update(sql,
                filmGenre.getFilmId(),
                filmGenre.getGenreId());
        return filmGenre;
    }

    @Override
    public void deleteByFilmId(int filmId) {
        var rowSet = "delete from FilmGenre WHERE FilmID = ?";
        jdbcTemplate.update(rowSet, filmId);
    }

    private FilmGenre mapToRow(SqlRowSet rowSet) {
        int genreId = rowSet.getInt("GenreID");
        int filmId = rowSet.getInt("FilmID");
        return FilmGenre.builder()
                .genreId(genreId)
                .filmId(filmId)
                .build();
    }
}