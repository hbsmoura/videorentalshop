package com.hbsmoura.videorentalshop.service;

import com.hbsmoura.videorentalshop.exceptions.UserNotFoundException;
import com.hbsmoura.videorentalshop.model.User;
import com.hbsmoura.videorentalshop.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Method for retrieve a user with given username
     * @param username the given username
     * @return the retrieved user
     * @throws UsernameNotFoundException if there is no user with the given username on the model layer
     *
     */

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("User not found with this username: " + username)
        );
    }

    /**
     * Method for retrieve a user with given id
     * @param id the given id
     * @return the retrieved user
     * @throws UserNotFoundException if there is no user with the given id on the model layer
     *
     */
    public User loadUserById(UUID id) {
        return userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException("User not found with this id: " + id)
        );
    }
}
