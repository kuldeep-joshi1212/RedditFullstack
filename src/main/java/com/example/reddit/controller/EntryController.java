package com.example.reddit.controller;

import com.example.reddit.Exception.PasswordException;
import com.example.reddit.Exception.UserException;
import com.example.reddit.model.Login;
import com.example.reddit.model.User;
import com.example.reddit.model.dto.UserDTO;
import com.example.reddit.service.LoginService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;


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
    ResponseEntity<UserDTO> login(@RequestBody Login userLogin) {
        try {
            UserDTO loggedInUser = loginService.validLogin(userLogin);
            if (Objects.nonNull(loggedInUser)) {
                return ResponseEntity.ok(loggedInUser);
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (UserException | PasswordException e) {
            log.error("Crucial information for user mission {}", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/signup")
    ResponseEntity<UserDTO> signup(@RequestBody User user) {
        try {
            UserDTO signedUpUser = loginService.signUpUser(user);
            if (Objects.nonNull(signedUpUser)) {
                return ResponseEntity.ok(signedUpUser);
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (UserException e) {
            log.error("User object is invalid {}", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
