package com.hbsmoura.videorentalshop.service;

import com.hbsmoura.videorentalshop.config.security.JwtService;
import com.hbsmoura.videorentalshop.dtos.UserLoginDto;
import com.hbsmoura.videorentalshop.model.Client;
import com.hbsmoura.videorentalshop.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.security.auth.login.FailedLoginException;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private UserService userService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    Client mockedClient = Client.builder()
            .id(UUID.randomUUID())
            .username("mockedclient")
            .password("password")
            .build();

    Authentication authentication =
            new UsernamePasswordAuthenticationToken(mockedClient, mockedClient.getPassword());

    @Test
    @DisplayName("Authenticate user test")
    void authenticateTest() throws FailedLoginException {
        UserLoginDto login = new ModelMapper().map(mockedClient, UserLoginDto.class);
        String fakeJwt = "Json.Web.Token";

        doReturn(mockedClient).when(userService).loadUserByUsername(anyString());
        doReturn(authentication).when(authenticationManager).authenticate(any(Authentication.class));
        doReturn(fakeJwt).when(jwtService).generateToken(any(User.class));

        String generatedToken = authService.authenticate(login);

        assertThat(generatedToken, is(fakeJwt));
    }

    @Test
    @DisplayName("Authenticate user throw FailedLoginException test")
    void authenticateThrowFailedLoginExceptionTest() {
        UserLoginDto login = new ModelMapper().map(mockedClient, UserLoginDto.class);

        doReturn(mockedClient).when(userService).loadUserByUsername(anyString());
        doThrow(BadCredentialsException.class).when(authenticationManager).authenticate(any(Authentication.class));

        assertThrows(FailedLoginException.class, () -> authService.authenticate(login));
    }

    @Test
    @DisplayName("Is itself test")
    void isItselfTest() {
        SecurityContextHolder.getContext().setAuthentication(authentication);

        doReturn(mockedClient).when(userService).loadUserById(any(UUID.class));

        assertTrue(authService.isItself(UUID.randomUUID()));
    }
}
