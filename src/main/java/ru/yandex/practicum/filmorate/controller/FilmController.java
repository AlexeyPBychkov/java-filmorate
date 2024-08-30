package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final Map<Long, Film> films = new HashMap<>();
    private static int MAX_DESCRIPTION_LENGTH = 200;
    private static String MIN_RELEASE_DATE = "28-12-1895";

    @GetMapping
    public Collection<Film> getAll() {
        return films.values();
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            log.warn("Название фильма не может быть пустым");
            throw new ValidationException("Название фильма не может быть пустым");
        }
        if (film.getDescription().length() > MAX_DESCRIPTION_LENGTH) {
            log.warn("Максимальная длина описания - 200 символов");
            throw new ValidationException("Максимальная длина описания - 200 символов");
        }
        Date startDate;
        try {
            startDate = new SimpleDateFormat("dd-MM-yyyy").parse(MIN_RELEASE_DATE);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        if (film.getReleaseDate().before(startDate)) {
            log.warn("Дата релиза — не раньше 28 декабря 1895 года");
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года");
        }
        if (film.getDuration() <= 0) {
            log.warn("Продолжительность фильма должна быть положительной");
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Фильм с id=" + film.getId() + " создан");
        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        if (!films.containsKey(film.getId())) {
            log.warn("Фильма с таким Id не существует");
            throw new ValidationException("Фильма с таким Id не существует");
        }
        if (film.getName() == null || film.getName().isBlank()) {
            log.warn("Название фильма не может быть пустым");
            throw new ValidationException("Название фильма не может быть пустым");
        }
        if (film.getDescription().length() > MAX_DESCRIPTION_LENGTH) {
            log.warn("Максимальная длина описания - 200 символов");
            throw new ValidationException("Максимальная длина описания - 200 символов");
        }
        Date startDate;
        try {
            startDate = new SimpleDateFormat("dd-MM-yyyy").parse(MIN_RELEASE_DATE);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        if (film.getReleaseDate().before(startDate)) {
            log.warn("Дата релиза — не раньше 28 декабря 1895 года");
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года");
        }
        if (film.getDuration() <= 0) {
            log.warn("Продолжительность фильма должна быть положительной");
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }
        Film oldFilm = films.get(film.getId());
        oldFilm.setDescription(film.getDescription());
        oldFilm.setDuration(film.getDuration());
        oldFilm.setReleaseDate(film.getReleaseDate());
        oldFilm.setName(film.getName());
        log.warn("Фильм с id=" + oldFilm.getId() + " обновлен");
        return oldFilm;
    }

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
