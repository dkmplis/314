package ru.kata.spring.boot_security.demo.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import javax.persistence.EntityNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;


@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRep, RoleRepository roleRepository) {
        this.userRepository = userRep;
        this.roleRepository = roleRepository;
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User findById(int id) {
        return userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Transactional
    public void addNew(User user) {
        userRepository.save(user);
    }

    @Transactional
    public void deleteById(int id) {
        userRepository.deleteById(id);
    }

    @Transactional
    public void update(int id, User updatedUser) {
        updatedUser.setId(id);
        userRepository.save(updatedUser);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Email not found!"));
    }

    @Override
    public Optional<User> findByEmailForValid(String email) {
        return userRepository.findByEmail(email);
    }

    public User addRoles(User user, List<Integer> rolesId) {
        HashSet<Role> selectedRoles = new HashSet<>();
        if (rolesId != null) {
            for (int roleId : rolesId) {
                Role role = roleRepository.findById(roleId)
                        .orElseThrow(EntityNotFoundException::new);
                selectedRoles.add(role);
            }
        }
        user.setRoles(selectedRoles);
        return user;
    }
}
