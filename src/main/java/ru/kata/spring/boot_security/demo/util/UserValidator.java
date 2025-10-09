package ru.kata.spring.boot_security.demo.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.kata.spring.boot_security.demo.dto.UserDTO;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.util.Optional;

@Component
public class UserValidator implements Validator {
    private final UserService userService;

    @Autowired
    public UserValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return UserDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserDTO userDTO = (UserDTO) target;
        if (userService.findByEmailForValid(userDTO.getEmail()).isPresent()) {
            errors.rejectValue("email", "", "The email exists");
        }
    }

    public void validateForEdit(Object target, Errors errors) {
        UserDTO userDTO = (UserDTO) target;
        Optional<User> existingUser = userService.findByEmailForValid(userDTO.getEmail());
        if (existingUser.isPresent()) {
            if (existingUser.get().getId() != userDTO.getId()) {
                errors.rejectValue("email", "", "The email exists");
            }
        }
    }
}
