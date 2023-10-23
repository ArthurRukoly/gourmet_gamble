package com.RTU.gourmetgamble.controllers;

import com.RTU.gourmetgamble.models.Product;
import com.RTU.gourmetgamble.models.Recipe;
import com.RTU.gourmetgamble.models.RecipeScore;
import com.RTU.gourmetgamble.repositories.ProductRepository;
import com.RTU.gourmetgamble.repositories.RecipeProductRepository;
import com.RTU.gourmetgamble.repositories.RecipeRepository;
import com.RTU.gourmetgamble.repositories.RecipeScoreRepository;
import com.RTU.gourmetgamble.services.AuthenticationService;
import com.RTU.gourmetgamble.services.ProductServices;
import com.RTU.gourmetgamble.services.RandomServices;
import com.RTU.gourmetgamble.services.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Controller
@RequiredArgsConstructor
public class ExactRecipeController {
    private final RecipeRepository recipeRepository;
    private final RecipeProductRepository recipeProductRepository;
    private final ProductServices productServices;
    private final RecipeService recipeService;
    private final AuthenticationService authenticationService;
    private final RecipeScoreRepository recipeScoreRepository;
    @GetMapping("/recipe/{id}")
    public String getRecipe(@PathVariable Long id, Model model) {
        Recipe recipe = recipeRepository.findRecipeById(id);
        List<Product> ingredients = productServices.getProductsFromIds(
                                    recipeProductRepository.findProductIDByRecipeID(id));
        System.out.println("------------------------------------------");
        for (Product p: ingredients) {
            System.out.println("ingred = " + p);
        }
        System.out.println("------------------------------------------");
        model.addAttribute("recipe", recipe);
        model.addAttribute("ingredients", ingredients);
        model.addAttribute("isAuthorized", authenticationService.checkIfUserIsAuthorized());
        return "exactRecipe"; // Return the view name
    }
}
