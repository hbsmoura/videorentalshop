package com.hbsmoura.videorentalshop.service;

import com.hbsmoura.videorentalshop.config.security.JwtService;
import com.hbsmoura.videorentalshop.dtos.UserLoginDto;
import com.hbsmoura.videorentalshop.exceptions.FailedLoginException;
import com.hbsmoura.videorentalshop.model.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthService {

    private final UserService userService;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    public AuthService(UserService userService, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    /**
     * Method for authenticate a user and retrieve JWT
     * @param login the user data to be authenticated
     * @return the generated JWT
     * @throws FailedLoginException if the user data does not match
     *
     */

    public String authenticate(UserLoginDto login) throws FailedLoginException {
        User user = userService.loadUserByUsername(login.getUsername());

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    user, login.getPassword()
            ));
        } catch (AuthenticationException ex) {
            throw new FailedLoginException("Login failed! Invalid username or password");
        }

        return jwtService.generateToken(user);
    }

    /**
     * Method for check if the user with the given id is the same as the authenticated one
     * @param id the given id
     * @return true if the user is the same
     *
     */

    public boolean isItself(UUID id) {
        User user = userService.loadUserById(id);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = user.getUsername();
        String authenticationName = authentication.getName();

        return username.equals(authenticationName);
    }
}
