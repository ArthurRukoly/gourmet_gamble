package com.RTU.gourmetgamble.controllers;

import com.RTU.gourmetgamble.models.Product;
import com.RTU.gourmetgamble.models.Recipe;
import com.RTU.gourmetgamble.repositories.ProductRepository;
import com.RTU.gourmetgamble.repositories.RecipeProductRepository;
import com.RTU.gourmetgamble.repositories.RecipeRepository;
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
        model.addAttribute("ingredients", ingredients);
        model.addAttribute("randomObject", randomRecipe);
        return "random";
    }
    @PostMapping("/random")
    public String updateRating(@RequestParam("receiptId") Long id, @RequestParam("rating") float rating) {
        // Check if the user is authenticated
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated()) {
            // User is authenticated, process the request
            Recipe currentRecipe = recipeService.getReceiptById(id);
            currentRecipe.setRating(currentRecipe.getRating() + rating);
            currentRecipe.setRatingCount(currentRecipe.getRatingCount() + 1);
            recipeRepository.save(currentRecipe);
        } else {
            System.out.println("nelzya");
        }

        return "redirect:/random";
    }
}
