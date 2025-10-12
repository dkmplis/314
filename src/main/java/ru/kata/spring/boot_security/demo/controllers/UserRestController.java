package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kata.spring.boot_security.demo.dto.UserDTO;
import ru.kata.spring.boot_security.demo.services.UserService;
import ru.kata.spring.boot_security.demo.services.dto.UserDtoService;

import java.security.Principal;

@RestController
@RequestMapping("/api/user")
public class UserRestController {
    private final UserDtoService userDtoService;
    private final UserService userService;

    public UserRestController(UserDtoService userDtoService, UserService userService) {
        this.userDtoService = userDtoService;
        this.userService = userService;
    }


    @GetMapping("/current")
    public ResponseEntity<UserDTO> getCurrent(Principal principal) {
        return new ResponseEntity<>(userDtoService
                .convertToDto(userService
                        .findByEmail(principal.getName())), HttpStatus.OK);
    }
}
