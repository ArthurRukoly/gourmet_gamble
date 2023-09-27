package com.RTU.gourmetgamble.controllers;

import com.RTU.gourmetgamble.models.Product;
import com.RTU.gourmetgamble.models.Recipe;
import com.RTU.gourmetgamble.repositories.ProductRepository;
import com.RTU.gourmetgamble.repositories.RecipeRepository;
import com.RTU.gourmetgamble.services.ProductServices;
import com.RTU.gourmetgamble.services.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/receipts")
public class RecipeController {

    private final RecipeRepository recipeRepository;
    private final ProductRepository productRepository;
    private final ProductServices productServices;
    private final RecipeService recipeService;

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("products", productServices.getAllProducts());
        model.addAttribute("receipt", new Recipe());
        return "create-receipt";
    }

//    @PostMapping("/create")
//    public String createReceipt(
//            @RequestParam("name") String name,
//            @RequestParam("rating") float rating,
//            @RequestParam("difficulty") float difficulty,
//            @RequestParam("products") List<Long> selectedProductIds
//    ) {
//
//        // Create a recipe and associate it with the selected products
//        Recipe recipe = new Recipe();
//        recipe.setName(name);
//        recipe.setRating(rating);
//        recipe.setDifficulty(difficulty);
//
//        List<Product> selectedProducts = productServices.getProductsByIds(selectedProductIds);
//        recipe.setProducts(selectedProducts);
//        recipeService.saveReceiptWithProducts(recipe);
//
//        return "redirect:/receipts/create";
//    }
}
