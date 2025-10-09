package ru.kata.spring.boot_security.demo.services.dto;

import ru.kata.spring.boot_security.demo.dto.UserDTO;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserDtoService {
    User convertToNewUser(UserDTO userDTO);

    User convertToUser(UserDTO userDTO);

    UserDTO convertToDto(User user);

    List<UserDTO> convertToListDto(List<User> users);
}
