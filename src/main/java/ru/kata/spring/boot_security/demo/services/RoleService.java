package ru.kata.spring.boot_security.demo.services;

import ru.kata.spring.boot_security.demo.model.Role;

import java.util.List;

public interface RoleService {
    List<Role> getAll();

    Role findById(int id);

    void addNew(Role role);

    void deleteById(int id);

    void update(int id, Role role);

    Role findByName(String username);
}
