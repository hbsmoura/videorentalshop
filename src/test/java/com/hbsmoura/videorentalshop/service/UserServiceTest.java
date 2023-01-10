package com.hbsmoura.videorentalshop.service;

import com.hbsmoura.videorentalshop.config.security.SecurityConfigTest;
import com.hbsmoura.videorentalshop.exceptions.UserNotFoundException;
import com.hbsmoura.videorentalshop.model.Client;
import com.hbsmoura.videorentalshop.model.User;
import com.hbsmoura.videorentalshop.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Import(SecurityConfigTest.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private User mockedUser;

    @BeforeEach
    public void setup() {
        mockedUser = Client.builder()
                .id(UUID.randomUUID())
                .name("User name")
                .username("username")
                .password(passwordEncoder.encode("pass"))
                .bookings(Collections.emptyList())
                .build();
    }

    @Test
    @DisplayName("Load user by username test")
    void loadUserByUsernameTest() {
        doReturn(Optional.of(mockedUser)).when(userRepository).findByUsername(anyString());

        UserDetails returnedUser = userService.loadUserByUsername("username");

        assertThat(returnedUser.getUsername(), is(mockedUser.getUsername()));
        passwordEncoder.matches("pass", returnedUser.getPassword());
    }

    @Test
    @DisplayName("Load user by username test throw UserIdNotFoundException test")
    void loadUserByUsernameThrowUserIdNotFoundExceptionTest() {
        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("username"));
    }

    @Test
    @DisplayName("Is itself test")
    void isItselfTest() {
        doReturn(Optional.of(mockedUser)).when(userRepository).findById(any(UUID.class));
        doReturn(authentication).when(securityContext).getAuthentication();

        SecurityContextHolder.setContext(securityContext);

        userService.isItself(mockedUser.getId());

        verify(userRepository, times(1)).findById(any(UUID.class));
        verify(securityContext, times(1)).getAuthentication();
    }

    @Test
    @DisplayName("Is itself throw UsernameNotFoundException test")
    void isItselfThrowUsernameNotFoundExceptionTest() {
        assertThrows(
                UserNotFoundException.class,
                () -> userService.isItself(UUID.randomUUID())
        );
    }
}
