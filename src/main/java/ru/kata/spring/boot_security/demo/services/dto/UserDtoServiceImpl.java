package ru.kata.spring.boot_security.demo.services.dto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.dto.RoleDTO;
import ru.kata.spring.boot_security.demo.dto.UserDTO;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserDtoServiceImpl implements UserDtoService {
    private final RoleRepository roleRepository;

    @Autowired
    public UserDtoServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public User convertToNewUser(UserDTO userDTO) {
        User user = new User();
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setAge(userDTO.getAge());
        user.setPassword(userDTO.getPassword());
        user.setEmail(userDTO.getEmail());
        user.setRoles(new HashSet<>());
        for (RoleDTO roleDTO : userDTO.getRoles()) {
            Role role = roleRepository.findById(roleDTO.getId()).orElseThrow(EntityNotFoundException::new);
            user.getRoles().add(role);
        }
        return user;
    }

    @Override
    public User convertToUser(UserDTO userDTO) {
        User user = convertToNewUser(userDTO);
        user.setId(userDTO.getId());
        return user;
    }

    @Override
    public UserDTO convertToDto(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setAge(user.getAge());
        userDTO.setPassword(user.getPassword());
        userDTO.setEmail(user.getEmail());
        userDTO.setRoles(new ArrayList<>());
        for (Role role : user.getRoles()) {
            userDTO.getRoles().add(new RoleDTO(role));
        }
        return userDTO;
    }

    @Override
    public List<UserDTO> convertToListDto(List<User> users) {
        return users.stream().map(this::convertToDto)
                .collect(Collectors.toList());
    }
}
