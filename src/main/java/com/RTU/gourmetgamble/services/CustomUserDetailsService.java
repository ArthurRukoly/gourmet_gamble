package com.RTU.gourmetgamble.services;

import lombok.RequiredArgsConstructor;
import com.RTU.gourmetgamble.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        if(userRepository.findByEmail(email) == null) {
            throw new UsernameNotFoundException("error");
        }
        return userRepository.findByEmail(email);
    }
}