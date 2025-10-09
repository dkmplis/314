package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;


@Controller
public class UserController {

    @GetMapping("/user")
    public String show(Model model, Principal principal) {
        return "/user/user-page";
    }
}
