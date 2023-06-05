package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.storage.FilmGenreStorage;

import java.util.ArrayList;
import java.util.List;

@Component
public class DBFilmGenreStorage extends DbStorage implements FilmGenreStorage {

    public DBFilmGenreStorage(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public List<FilmGenre> getAllFilmGenre() {
        List<FilmGenre> filmGenres = new ArrayList<>();
        SqlRowSet RowSet = jdbcTemplate.queryForRowSet("select FilmID, GenreID from FilmGenre");
        while (RowSet.next()) {
            FilmGenre filmGenre = mapToRow(RowSet);
            filmGenres.add(filmGenre);
        }
        return filmGenres;
    }

    @Override
    public List<FilmGenre> getLikesFilmId(int filmId) {
        List<FilmGenre> filmGenres = new ArrayList<>();

        SqlRowSet RowSet = jdbcTemplate.queryForRowSet("select FilmID, GenreID from FilmGenre where FilmID = ?", filmId);

        while (RowSet.next()) {
            var filmGenre = mapToRow(RowSet);
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
        var Query = "delete from FilmGenre WHERE FilmID = ?";
        jdbcTemplate.update(Query, filmId);
    }

    private FilmGenre mapToRow(SqlRowSet RowSet) {
        int genreId = RowSet.getInt("GenreID");
        int filmId = RowSet.getInt("FilmID");
        return FilmGenre.builder()
                .genreId(genreId)
                .filmId(filmId)
                .build();
    }
}