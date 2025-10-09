package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kata.spring.boot_security.demo.dto.RoleDTO;
import ru.kata.spring.boot_security.demo.dto.UserDTO;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;
import ru.kata.spring.boot_security.demo.services.dto.RoleDtoService;
import ru.kata.spring.boot_security.demo.services.dto.UserDtoService;
import ru.kata.spring.boot_security.demo.util.UserValidator;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminRestController {
    private final UserService userService;
    private final UserValidator userValidator;
    private final RoleService roleService;
    private final UserDtoService userDtoService;
    private final RoleDtoService roleDtoService;

    @Autowired
    public AdminRestController(UserService userService, UserValidator userValidator,
                               RoleService roleService, UserDtoService serviceDTO,
                               RoleDtoService rolesDtoService) {
        this.userService = userService;
        this.userValidator = userValidator;
        this.roleService = roleService;
        this.userDtoService = serviceDTO;
        this.roleDtoService = rolesDtoService;
    }

    @GetMapping("/")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<User> users = userService.getAll();
        return new ResponseEntity<>(userDtoService.convertToListDto(users),
                HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable int id) {
        return new ResponseEntity<>(userDtoService.convertToDto(userService.findById(id)),
                HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> addNewUser(@RequestBody @Valid UserDTO userDTO,
                                        BindingResult bindingResult) throws MethodArgumentNotValidException {
        userValidator.validate(userDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new MethodArgumentNotValidException(null, bindingResult);
        }
        userService.addNew(userDtoService.convertToNewUser(userDTO));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editUser(@RequestBody @Valid UserDTO userDTO,
                                      BindingResult bindingResult,
                                      @PathVariable int id) throws MethodArgumentNotValidException {
        userValidator.validateForEdit(userDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new MethodArgumentNotValidException(null, bindingResult);
        }
        userService.update(id, userDtoService.convertToUser(userDTO));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable int id) {
        userService.deleteById(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/roles")
    public List<RoleDTO> getAllRoles() {
        return roleDtoService.convertToListDto(roleService.getAll());
    }

    @GetMapping("/current")
    public ResponseEntity<UserDTO> getCurrent(Principal principal) {
        return new ResponseEntity<>(userDtoService
                .convertToDto(userService
                        .findByEmail(principal.getName())), HttpStatus.OK);
    }
}
