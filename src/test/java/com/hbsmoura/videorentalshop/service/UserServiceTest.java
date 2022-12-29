package com.hbsmoura.videorentalshop.service;

import com.hbsmoura.videorentalshop.model.Client;
import com.hbsmoura.videorentalshop.model.User;
import com.hbsmoura.videorentalshop.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Test
    @DisplayName("Load user by username test")
    void loadUserByUsernameTest() {
        User user = Client.builder()
                .id(UUID.randomUUID())
                .name("User name")
                .username("username")
                .password(passwordEncoder.encode("pass"))
                .bookings(Collections.emptyList())
                .build();

        doReturn(Optional.of(user)).when(userRepository).findByUsername(anyString());

        UserDetails returnedUser = userService.loadUserByUsername("username");

        assertThat(returnedUser.getUsername(), is(user.getUsername()));
        passwordEncoder.matches("pass", returnedUser.getPassword());
    }

    @Test
    @DisplayName("Load user by username test throw UsernameNotFoundException test")
    void loadUserByUsernameThrowUsernameNotFoundExceptionTest() {
        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("username"));
    }
}
