package com.RTU.gourmetgamble.controllers;

import com.RTU.gourmetgamble.models.Product;
import com.RTU.gourmetgamble.models.Recipe;
import com.RTU.gourmetgamble.models.RecipeScore;
import com.RTU.gourmetgamble.repositories.ProductRepository;
import com.RTU.gourmetgamble.repositories.RecipeProductRepository;
import com.RTU.gourmetgamble.repositories.RecipeRepository;
import com.RTU.gourmetgamble.repositories.RecipeScoreRepository;
import com.RTU.gourmetgamble.services.AuthenticationService;
import com.RTU.gourmetgamble.services.RandomServices;
import com.RTU.gourmetgamble.services.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class RandomController {

    private final RecipeRepository recipeRepository;
    private final RecipeProductRepository recipeProductRepository;
    private final ProductRepository productRepository;
    private final RandomServices randomServices;
    private final RecipeService recipeService;
    private final AuthenticationService authenticationService;
    private final RecipeScoreRepository recipeScoreRepository;

    @GetMapping("/random")
    public String home(Model model) {
        Recipe randomRecipe = randomServices.getRandomReceipt();
        List<Long> productsIDs = recipeProductRepository.findProductIDByRecipeID(randomRecipe.getId());
        List<Product> ingredients = new ArrayList<>();
        System.out.println("id recipe " + randomRecipe.getId());
        for (Long id: productsIDs) {
            Product product = productRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
            ingredients.add(product);
        }

        Boolean isAuthorized = authenticationService.checkIfUserIsAuthorized();
        model.addAttribute("ingredients", ingredients);
        model.addAttribute("randomObject", randomRecipe);
        model.addAttribute("isAuthorized", isAuthorized);
        return "random";
    }
    @PostMapping("/random")
    public String updateRating(@RequestParam("recipeId") Long recipeId, @RequestParam("rating") String ratingString) {
        // Check if the user is authenticated
        float rating = 0;
        if (!ratingString.isEmpty()){
            rating = Float.valueOf(ratingString);
        }
        if (authenticationService.checkIfUserIsAuthorized()) {
            Long currentUserID = authenticationService.getAutorizedUserId();
            System.out.println("recipeid " + recipeId + " rating " + rating);

            RecipeScore newScore = new RecipeScore();
            newScore.setRecipeId(recipeId);
            newScore.setUserId(currentUserID);
            newScore.setRating(rating);

            recipeScoreRepository.save(newScore);
            Recipe currentRecipe = recipeRepository.findRecipeById(recipeId);
            recipeService.setScore(currentRecipe);
            recipeRepository.save(currentRecipe);
            return "redirect:/main";
        } else {
            System.out.println("nelzya");
        }

        return "redirect:/user/login";
    }
}
