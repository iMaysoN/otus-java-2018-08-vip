package ru.otus.controllers;

import org.springframework.web.bind.annotation.*;
import ru.otus.services.DBService;
import ru.otus.services.User;

import java.util.List;

@RestController
public class UserController {
    private final DBService dbService;

    public UserController(DBService dbService) {
        this.dbService = dbService;
    }

    @GetMapping({"/users", "/users/"})
    public List<User> getUsers() {
        return dbService.getAll(User.class);
    }

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable("id") Long id) {
        return dbService.read(id);
    }

    @PostMapping("/users")
    public User addUser(@RequestBody User user) {
        long userId = dbService.save(user);
        return dbService.read(userId);
    }
}
