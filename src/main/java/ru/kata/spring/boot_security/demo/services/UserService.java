package ru.kata.spring.boot_security.demo.services;

import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User addRoles(User user, List<Integer> rolesId);

    List<User> getAll();

    User findById(int id);

    void addNew(User user);

    void deleteById(int id);

    void update(int id, User user);

    User findByEmail(String email);

    Optional<User> findByEmailForValid(String email);
}
