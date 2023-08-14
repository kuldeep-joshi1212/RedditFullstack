package com.example.reddit.service;

import com.example.reddit.Exception.PasswordException;
import com.example.reddit.Exception.UserNameException;
import com.example.reddit.model.Login;
import com.example.reddit.model.User;
import com.example.reddit.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class LoginService {

    private final UserRepository userRepository;

    @Autowired
    public LoginService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean validLogin(Login userLogin) throws UserNameException, PasswordException {
        if (Boolean.FALSE.equals(StringUtils.hasText(userLogin.getUsername()))) {
            throw new UserNameException("user name not sent for customer");
        }

        if (Boolean.FALSE.equals(StringUtils.hasText(userLogin.getPassword()))) {
            throw new PasswordException("Pass word not sent for customer");
        }

        User user = userRepository.findByUsername(userLogin.getUsername());

        return userLogin.getPassword().equals(user.getPassword());
    }
}
