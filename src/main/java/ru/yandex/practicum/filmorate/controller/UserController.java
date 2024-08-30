package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> getAll() {
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody User user) {
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            log.warn("Почта не может быть пустой");
            throw new ValidationException("Почта не может быть пустой");
        }
        if (!user.getEmail().contains("@")) {
            log.warn("Почта должна сожержать знак @");
            throw new ValidationException("Почта должна сожержать знак @");
        }
        if (user.getLogin() == null || user.getLogin().isBlank()) {
            log.warn("Логин не может быть пустым");
            throw new ValidationException("Логин не может быть пустым");
        }
        if (user.getLogin().contains(" ")) {
            log.warn("Логин не может содержать пробелы");
            throw new ValidationException("Логин не может содержать пробелы");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        if (user.getBirthday().toInstant().isAfter(Instant.now())) {
            log.warn("Дата рождения не может быть в будущем");
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
        user.setId(getNextId());
        users.put(user.getId(), user);
        log.info("Пользователь " + user.getName() + " создан");
        return user;
    }

    @PutMapping
    public User update(@RequestBody User user) {
        if (!users.containsKey(user.getId())) {
            log.warn("Пользователя с таким Id не существует");
            throw new ValidationException("Пользователя с таким Id не существует");
        }
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            log.warn("Почта не может быть пустой");
            throw new ValidationException("Почта не может быть пустой");
        }
        if (!user.getEmail().contains("@")) {
            log.warn("Почта должна сожержать знак @");
            throw new ValidationException("Почта должна сожержать знак @");
        }
        if (user.getLogin() == null || user.getLogin().isBlank()) {
            log.warn("Логин не может быть пустым");
            throw new ValidationException("Логин не может быть пустым");
        }
        if (user.getLogin().contains(" ")) {
            log.warn("Логин не может содержать пробелы");
            throw new ValidationException("Логин не может содержать пробелы");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().after(Date.from(Instant.now()))) {
            log.warn("Дата рождения не может быть в будущем");
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
        User oldUser = users.get(user.getId());
        oldUser.setBirthday(user.getBirthday());
        oldUser.setEmail(user.getEmail());
        oldUser.setLogin(user.getLogin());
        oldUser.setName(user.getName());
        log.info("Пользователь " + oldUser.getName() + " обновлен");
        return oldUser;
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
