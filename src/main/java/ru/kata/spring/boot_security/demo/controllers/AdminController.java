package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.services.RoleServiceImpl;
import ru.kata.spring.boot_security.demo.services.UserServiceImpl;
import ru.kata.spring.boot_security.demo.util.UserValidator;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/admin")
@Controller
public class AdminController {

    private final UserServiceImpl userService;
    private final RoleServiceImpl roleService;
    private final PasswordEncoder passwordEncoder;
    private final UserValidator userValidator;

    @Autowired
    public AdminController(UserServiceImpl userService, RoleServiceImpl roleService, PasswordEncoder passwordEncoder, UserValidator userValidator) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.userValidator = userValidator;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("admin", getCurrentUser());
        model.addAttribute("users", userService.getAll());
        model.addAttribute("newUser", new User());
        model.addAttribute("roles", roleService.getAll());
        return "admin/admin-page";
    }


    @PostMapping("/new")
    public String addNewUserByAnAdmin(@Valid @ModelAttribute("user") User user,
                                      BindingResult bindingResult,
                                      @RequestParam(value = "rolesId", required = false) List<Integer> rolesId,
                                      Model model) {
        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("admin", getCurrentUser());
            model.addAttribute("roles", roleService.getAll());
            model.addAttribute("users", userService.getAll());
            return "admin/admin-page";
        }
        User newUser = userService.addRoles(user, rolesId);
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        userService.addNew(newUser);
        return "redirect:/admin/";
    }

    @DeleteMapping("/delete")
    public String deleteUser(@RequestParam(value = "id") int id) {
        userService.deleteById(id);
        return "redirect:/admin/";
    }


    @PatchMapping("/edit")
    public String update(@RequestParam("id") int id,
                         @RequestParam("firstName") String firstName,
                         @RequestParam("lastName") String lastName,
                         @RequestParam("age") int age,
                         @RequestParam("email") String email,
                         @RequestParam(value = "password") String password,
                         @RequestParam(value = "rolesId") List<Integer> rolesId,
                         Model model) {
        User user = new User();
        user.setId(id);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setAge(age);
        user.setEmail(email);
        user.setPassword(password);

        BindingResult bindingResult = new BeanPropertyBindingResult(user, "user");
        userValidator.validateForEdit(user, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("users", userService.getAll());
            model.addAttribute("roles", roleService.getAll());
            model.addAttribute("newUser", new User());
            model.addAttribute("admin", getCurrentUser());
            return "admin/admin-page";
        }

        User updatesUser = userService.addRoles(user, rolesId);
        userService.update(user.getId(), updatesUser);
        return "redirect:/admin/";
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User not authenticated");
        }
        return userService.findByEmail(authentication.getName());
    }



}
