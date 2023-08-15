package com.example.reddit.controller;

import com.example.reddit.Exception.PasswordException;
import com.example.reddit.Exception.UserException;
import com.example.reddit.model.Login;
import com.example.reddit.model.User;
import com.example.reddit.service.LoginService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@Log4j2
public class EntryController {

    private final LoginService loginService;

    @Autowired
    public EntryController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/login")
    boolean login(@RequestBody Login userLogin) {
        try {
            return loginService.validLogin(userLogin);
        } catch (UserException | PasswordException e) {
           log.error("Crucial information for user mission {}", e);
           return false;
        }
    }

    @PostMapping("/signup")
    boolean signup(@RequestBody User user){
        try {
            return loginService.signUpUser(user);
        } catch (UserException e) {
            log.error("User object is invalid {}", e);
            return false;
        }
    }

}
