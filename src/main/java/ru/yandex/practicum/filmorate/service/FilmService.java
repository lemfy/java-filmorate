package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final MpaStorage mpaStorage;
    private final GenreStorage genreStorage;
    private final LikesStorage likesStorage;
    private final FilmGenreStorage filmGenreStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, MpaStorage mpaStorage,
                           GenreStorage genreStorage, LikesStorage likesStorage,
                           FilmGenreStorage filmGenreStorage) {
        this.filmStorage = filmStorage;
        this.mpaStorage = mpaStorage;
        this.genreStorage = genreStorage;
        this.likesStorage = likesStorage;
        this.filmGenreStorage = filmGenreStorage;
    }

    public Film createFilm(Film film) {
        validate(film);
        filmStorage.createFilm(film);
        addGenreForFilm(film);
        return film;
    }

    public Film changeFilm(Film film) {
        validate(film);
        log.info("Запрос изменения фильма1");
        try {
            filmStorage.changeFilm(film);
        } catch (Exception e) {
            throw new FilmNotFoundException("Такого фильма не существует!");
        }
        log.info("Запрос изменения фильма2");
        filmGenreStorage.deleteByFilmId(film.getId());
        log.info("Запрос изменения фильма3");
        addGenreForFilm(film);
        log.info("Запрос изменения фильма4");
        return film;
    }

    public List<Film> findAllFilms() {
        log.info("Запрос фильмов");
        var films = filmStorage.findAllFilms();
        var mpaList = mpaStorage.getAllMpa();
        var genres = genreStorage.getAllGenres();
        var filmGenres = filmGenreStorage.getAllFilmGenre();
        var likes = likesStorage.getAllLikes();
        for (var film : films) {
            setAllPar(film, mpaList, genres, filmGenres, likes);
        }
        return films;
    }

    public Film findFilmById(int filmId) {
        log.info("Запрос фильма по id");
        var film = filmStorage.findFilmById(filmId);
        var mpaList = mpaStorage.getAllMpa();
        var genres = genreStorage.getAllGenres();
        var filmGenres = filmGenreStorage.getLikesFilmId(film.getId());
        var likes = likesStorage.getLikesWithFilmId(film.getId());
        setAllPar(film, mpaList, genres, filmGenres, likes);
        return film;
    }

    public void addLike(int userId, int filmId) {
        Likes like = likesStorage.getLikesCurrentUserWithFilmId(userId, filmId);
        if (like == null) {
            likesStorage.addLike(new Likes(filmId, userId));
        }
    }

    public void removeLike(int userId, int filmId) {
        Likes like = likesStorage.getLikesCurrentUserWithFilmId(userId, filmId);
        if (like == null) {
            throw new FilmNotFoundException("лайк не найден");
        }
        likesStorage.removeLike(new Likes(filmId, userId));
    }

    public List<Film> getBestFilms(int count) {
        var films = findAllFilms();
        return films.stream().sorted(Comparator.comparingInt(film -> film.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }

    public Genres getGenres(int id) {
        return genreStorage.findGenreById(id);
    }

    public List<Genres> getGenresList() {
        return genreStorage.getAllGenres();
    }

    public Mpa getMpa(int id) {
        return mpaStorage.getMpaById(id);
    }

    public List<Mpa> getMpaList() {
        var mpaList = mpaStorage.getAllMpa();
        List<Mpa> list = new ArrayList<>(mpaList);
        list.sort(Comparator.comparing(Mpa::getId));
        return list;
    }

    private void addGenreForFilm(Film film) {
        if (film.getGenres() != null && film.getGenres().size() > 0) {
            List<Genres> ratingList = film.getGenres();
            Set<Integer> uniqueId = new HashSet<>();
            ratingList.removeIf(rating -> !uniqueId.add(rating.getId()));
            film.setGenres(ratingList);
            for (var genre : film.getGenres()) {
                var filmGenre = new FilmGenre(film.getId(), genre.getId());
                filmGenreStorage.add(filmGenre);
            }
        }
    }

    private void setAllPar(Film film, Set<Mpa> mpaList, List<Genres> genres,
                           List<FilmGenre> filmGenres, Set<Likes> likes) {
        List<Genres> genreByFilm = new ArrayList<>();
        filmGenres.stream().filter(f -> f.getFilmId() == film.getId())
                .forEach(f -> genreByFilm.add(new Genres(f.getGenreId(),
                                genres.stream().filter(g -> g.getId() == f.getGenreId()).findAny().get().getName())));

        film.setGenres(genreByFilm);
        film.getMpa().setName(mpaList.stream().filter(m -> m.getId() == film.getMpa().getId()).findAny().get().getName());
        Set<Integer> likesByFilm = new HashSet<>();
        likes.stream().filter(l -> l.getFilmId() == film.getId()).forEach(l -> likesByFilm.add(l.getUserId()));
        film.setLikes(likesByFilm);

    }

    public void validate(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.error("Film releaseDate is invalid. {}", film.getName());
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года");
        }
        if (film.getName() == null || film.getName().isEmpty() || film.getName().isBlank()) {
            log.error("Film name is empty or invalid. {}", film.getId());
            throw new ValidationException("Имя не может быть пустым");
        }
        if ((film.getDescription().length()) > 200) {
            log.error("Film description is too long. {}", film.getName());
            throw new ValidationException("Описание фильма больше 200 символов");
        }
        if (film.getDuration() <= 0) {
            log.error("Film duration is negative. {}", film.getName());
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }
    }
}