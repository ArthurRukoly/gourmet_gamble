package com.RTU.gourmetgamble.services;

import com.RTU.gourmetgamble.models.RecipeScore;
import com.RTU.gourmetgamble.models.User;
import com.RTU.gourmetgamble.repositories.RecipeScoreRepository;
import com.RTU.gourmetgamble.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RecipeScoreRepository recipeScoreRepository;
    public boolean createUser(User user){

        if(userRepository.findByEmail(user.getEmail()) != null) return false;
        user.setActive(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.saveAndFlush(user);
        return true;
    }

}
