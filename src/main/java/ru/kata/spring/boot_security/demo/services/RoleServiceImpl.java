package ru.kata.spring.boot_security.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<Role> getAll() {
        return roleRepository.findAll();
    }

    @Override
    public Role findById(int id) {
        return roleRepository.findById(id)
                .orElseThrow(EntityExistsException::new);
    }

    @Override
    @Transactional
    public void addNew(Role role) {
        roleRepository.save(role);
    }

    @Override
    @Transactional
    public void deleteById(int id) {
        roleRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void update(int id, Role updatesRole) {
        updatesRole.setId(id);
        roleRepository.save(updatesRole);
    }

    @Override
    public Role findByName(String username) {
        return roleRepository.findByName(username)
                .orElseThrow(EntityNotFoundException::new);
    }

}
