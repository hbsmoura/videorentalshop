package com.hbsmoura.videorentalshop.controller;

import com.hbsmoura.videorentalshop.config.apiresponse.ApiResponseForbidden;
import com.hbsmoura.videorentalshop.config.apiresponse.ApiResponseUnauthorized;
import com.hbsmoura.videorentalshop.dtos.UserLoginDto;
import com.hbsmoura.videorentalshop.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name = "Auth Controller")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping
    @PreAuthorize("isAnonymous()")
    @Operation(
            summary = "Authenticate user",
            description = "Authenticates an user and retrieves an access token"
    )
    @ApiResponseUnauthorized
    @ApiResponseForbidden
    public String authenticate(@RequestBody @Valid UserLoginDto login) {
        return authService.authenticate(login);
    }
}
