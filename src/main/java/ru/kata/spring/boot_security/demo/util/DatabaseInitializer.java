package ru.kata.spring.boot_security.demo.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;

import javax.annotation.PostConstruct;

@Component
public class DatabaseInitializer {

    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DatabaseInitializer(UserService userService, RoleService roleService, PasswordEncoder passwordEncoder) {

        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    @Transactional
    public void init() {
        initializeRoles();
        initializeUsers();
    }

    private void initializeRoles() {
        if (roleService.getAll().isEmpty()) {
            Role admin = new Role("ROLE_ADMIN");
            roleService.addNew(admin);
            Role user = new Role("ROLE_USER");
            roleService.addNew(user);
        }
    }

    private void initializeUsers() {
        if (userService.getAll().isEmpty()) {
            Role adminRole = roleService.findByName("ROLE_ADMIN");
            Role userRole = roleService.findByName("ROLE_USER");
            User userAdmin = new User("admin", "admin", 20, "admin", "admin@mail.ru", adminRole);
            userService.addNew(userAdmin);
            User userUser = new User("user", "user", 29, "user", "user@mail.ru", userRole);
            userService.addNew(userUser);
        }

    }
}
