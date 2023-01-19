package com.hbsmoura.videorentalshop.controller;

import com.hbsmoura.videorentalshop.dtos.UserLoginDto;
import com.hbsmoura.videorentalshop.service.AuthService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.security.auth.login.LoginException;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping
    @PreAuthorize("isAnonymous()")
    public String authenticate(@RequestBody UserLoginDto login) throws LoginException {
        return authService.authenticate(login);
    }
}
