package com.hbsmoura.videorentalshop.service;

import com.hbsmoura.videorentalshop.exceptions.UserIdNotFoundException;
import com.hbsmoura.videorentalshop.model.User;
import com.hbsmoura.videorentalshop.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("userService")
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("User not found with this username: " + username)
        );
    }

    public boolean isItself(UUID id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new UserIdNotFoundException("User not found with this id: " + id)
        );
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = user.getUsername();
        String authenticationName = authentication.getName();

        return username.equals(authenticationName);
    }
}
