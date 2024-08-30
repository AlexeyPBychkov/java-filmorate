package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.util.Date;

/**
 * Film.
 */
@Data
public class Film {

    private long id;
    private String name;
    private String description;
    private Date releaseDate;
    private int duration;
}
