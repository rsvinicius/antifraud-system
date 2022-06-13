package com.example.antifraudsystem.service;

import com.example.antifraudsystem.model.entity.User;
import com.example.antifraudsystem.model.entity.UserDetailsImpl;
import com.example.antifraudsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    public static final String NOT_FOUND = "Not found: ";
    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = Optional
                .ofNullable(userRepository.findUserByUsernameIgnoreCase(username))
                .orElseThrow(() -> new UsernameNotFoundException(NOT_FOUND + username));

        return new UserDetailsImpl(user);
    }
}
