package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootTest
class FilmorateApplicationTests {

	static Film film;
	User user;

	@BeforeEach
	void prepare() {
		film = new Film();
		film.setName("Name");
		film.setDuration(90);
		film.setDescription("Description");
		film.setId(123);
        try {
            film.setReleaseDate(new SimpleDateFormat("dd-MM-yyyy").parse("01-01-2020"));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

	@Test
	void contextLoads() {
	}

	@Test
	void createFilmSuccess() {
		Film createdFilm = new FilmController().create(film);
		Assertions.assertEquals(createdFilm.toString(), film.toString());
	}

	@Test
	void createFilmWithoutName() {
		film.setName("");
		Film createdFilm = null;
		try {
			createdFilm = new FilmController().create(film);
		} catch (RuntimeException e) {
			System.out.println("Значение фильма будет null");
		}
		Assertions.assertNull(createdFilm);
	}

	@Test
	void createFilmWithIncorrectDate() {
		Date startDate;
		try {
			startDate = new SimpleDateFormat("dd-MM-yyyy").parse("01-01-1800");
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}

		film.setReleaseDate(startDate);
		Film createdFilm = null;
		try {
			createdFilm = new FilmController().create(film);
		} catch (RuntimeException e) {
			System.out.println("Значение фильма будет null");
		}
		Assertions.assertNull(createdFilm);
	}

	@Test
	void createFilmWithIncorrectDuration() {
		film.setDuration(-1);
		Film createdFilm = null;
		try {
			createdFilm = new FilmController().create(film);
		} catch (RuntimeException e) {
			System.out.println("Значение фильма будет null");
		}
		Assertions.assertNull(createdFilm);
	}

}
