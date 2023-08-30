package com.example.reddit.service;

import com.example.reddit.Exception.PasswordException;
import com.example.reddit.Exception.UserException;
import com.example.reddit.config.passwordConfig.PasswordConfig;
import com.example.reddit.config.securityConfig.UserAuthProvider;
import com.example.reddit.model.Login;
import com.example.reddit.model.User;
import com.example.reddit.model.dto.UserDTO;
import com.example.reddit.model.mappers.UserMapper;
import com.example.reddit.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.nio.CharBuffer;
import java.util.Objects;

@Service
@Log4j2
public class LoginService {

    private final UserRepository userRepository;
    private final PasswordConfig passwordEncoder;
    private final UserAuthProvider userAuthProvider;
    private final UserMapper userMapper;

    @Autowired
    public LoginService(UserRepository userRepository,
                        PasswordConfig passwordEncoder,
                        UserAuthProvider userAuthProvider,
                        UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userAuthProvider = userAuthProvider;
        this.userMapper = userMapper;
    }

    public UserDTO validLogin(Login userLogin) throws UserException, PasswordException {
        if (Boolean.FALSE.equals(StringUtils.hasText(userLogin.getUsername()))) {
            throw new UserException("Username not sent for customer");
        }

        if (Boolean.FALSE.equals(StringUtils.hasText(userLogin.getPassword()))) {
            throw new PasswordException("Password not sent for customer");
        }

        User user = userRepository.findByUsername(userLogin.getUsername());

        if (Objects.isNull(user)) {
            throw new UserException("user not found");
        }
        user.setToken(userAuthProvider.createToken(user));
        userRepository.save(user);
        if (passwordEncoder.passwordEncoder().matches(CharBuffer.wrap(userLogin.getPassword()), user.getPassword())) {
            return userMapper.toUserDTO(user);
        } else {
            throw new UserException("Incorrect Password");
        }
    }

    public UserDTO signUpUser(User user) throws UserException {
        if (Boolean.FALSE.equals(validateUserObject(user))) {
            throw new UserException("Invalid User Object");
        }
        User userAlreadyExists = userRepository.findByUsername(user.getUsername());
        if (Objects.nonNull(userAlreadyExists)) {
            return userMapper.toUserDTO(userAlreadyExists);
        }
        user.setToken(userAuthProvider.createToken(user));
        user.setPassword(passwordEncoder.passwordEncoder().encode(CharBuffer.wrap(user.getPassword())));
        User savedUser = userRepository.save(user);
        return userMapper.toUserDTO(savedUser);
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
