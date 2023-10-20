package com.RTU.gourmetgamble.controllers;

import com.RTU.gourmetgamble.models.Recipe;
import com.RTU.gourmetgamble.models.RecipeScore;
import com.RTU.gourmetgamble.repositories.RecipeRepository;
import com.RTU.gourmetgamble.repositories.RecipeScoreRepository;
import com.RTU.gourmetgamble.services.AuthenticationService;
import com.RTU.gourmetgamble.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserScoreController {
    private final RecipeScoreRepository recipeScoreRepository;
    private final AuthenticationService authenticationService;
    private final RecipeRepository recipeRepository;
    @GetMapping("/user/scores")
    public String getUsersScore(Model model) {
        if (authenticationService.checkIfUserIsAuthorized()){
            List<RecipeScore> userScores = recipeScoreRepository.findAllScoreOfUser(authenticationService.getAutorizedUserId());
            List<Recipe> recipeNames = new ArrayList<>();
            List<Float> scores = new ArrayList<>();
            for (RecipeScore r: userScores) {
                recipeNames.add(recipeRepository.findRecipeById(r.getRecipeId()));
                scores.add(r.getRating());
            }
            model.addAttribute("recipeNames", recipeNames);
            model.addAttribute("scores", scores);

            return "userScores";
        }
        else {
            return  "redirect:/user/login";
        }
    }
}
