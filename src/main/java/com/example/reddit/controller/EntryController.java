package com.example.reddit.controller;

import com.example.reddit.Exception.PasswordException;
import com.example.reddit.Exception.UserException;
import com.example.reddit.model.Login;
import com.example.reddit.model.User;
import com.example.reddit.service.LoginService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/user")
@Log4j2
//@CrossOrigin(origins = {"${ANGULAR.url}"})
public class EntryController {
    private final LoginService loginService;

    @Autowired
    public EntryController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/login")
    ResponseEntity<HttpStatus> login(@RequestBody Login userLogin) {
        try {
            boolean isValidLogin = loginService.validLogin(userLogin);
            if (isValidLogin) {
                return new ResponseEntity<>(HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (UserException | PasswordException e) {
            log.error("Crucial information for user mission {}", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/signup")
    ResponseEntity<HttpStatus> signup(@RequestBody User user) {
        try {
            boolean isValidSignUp = loginService.signUpUser(user);
            if (isValidSignUp) {
                return new ResponseEntity<>(HttpStatus.CREATED);
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (UserException e) {
            log.error("User object is invalid {}", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
