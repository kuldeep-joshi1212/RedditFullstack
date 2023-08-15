package com.example.reddit.service;

import com.example.reddit.Exception.PasswordException;
import com.example.reddit.Exception.UserException;
import com.example.reddit.model.Login;
import com.example.reddit.model.User;
import com.example.reddit.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Objects;

@Service
@Log4j2
public class LoginService {

    private final UserRepository userRepository;

    @Autowired
    public LoginService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean validLogin(Login userLogin) throws UserException, PasswordException {
        if (Boolean.FALSE.equals(StringUtils.hasText(userLogin.getUsername()))) {
            throw new UserException("user name not sent for customer");
        }

        if (Boolean.FALSE.equals(StringUtils.hasText(userLogin.getPassword()))) {
            throw new PasswordException("Pass word not sent for customer");
        }

        User user = userRepository.findByUsername(userLogin.getUsername());

        if (Objects.isNull(user)) {
            throw new UserException("user not found");
        }

        return userLogin.getPassword().equals(user.getPassword());
    }

    public boolean signUpUser(User user) throws UserException {
        if (Boolean.FALSE.equals(validateUserObject(user))) {
            return false;
        }
        User userAlreadyExists = userRepository.findByUsername(user.getUsername());
        if (Objects.nonNull(userAlreadyExists)) {
            return true;
        }

        User savedUser = userRepository.save(user);

        return user.getEmail().equals(savedUser.getEmail());
    }

    private Boolean validateUserObject(User user) throws UserException {
        if (Objects.isNull(user)
                || Boolean.FALSE.equals(StringUtils.hasText(user.getEmail()))
                || Boolean.FALSE.equals(StringUtils.hasText(user.getPassword()))
                || Boolean.FALSE.equals(StringUtils.hasText(user.getUsername()))) {
            throw new UserException("User object is not valid");
        }
        return Boolean.TRUE;
    }
}
