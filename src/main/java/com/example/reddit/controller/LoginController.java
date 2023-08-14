package com.example.reddit.controller;

import com.example.reddit.Exception.PasswordException;
import com.example.reddit.Exception.UserNameException;
import com.example.reddit.model.Login;
import com.example.reddit.service.LoginService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller("/user")
@Log4j2
public class LoginController {

    private final LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/login")
    boolean login(@RequestBody Login userLogin) {
        try {
            return loginService.validLogin(userLogin);
        } catch (UserNameException | PasswordException e) {
           log.error("Crucial information for user mission {}", e);
           return false;
        }
    }
}
