package com.udacity.jwdnd.course1.cloudstorage.controller;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {

    @GetMapping("/")
    public String defaultLoginView() {
        return loginView();
    }

    @GetMapping("/login")
    public String loginView() {

        // Only direct user to login page if they are not already logged in
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || (authentication != null && authentication instanceof AnonymousAuthenticationToken)) {
            return "login";
        }

        // Otherwise, redirect back home
        return "redirect:/home";
    }

}