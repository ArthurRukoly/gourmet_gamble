//package com.RTU.gourmetgamble.controllers;
//
//
//import com.RTU.gourmetgamble.models.Product;
//import com.RTU.gourmetgamble.requests.ProductAPI;
//import com.RTU.gourmetgamble.requests.RecipeAPI;
//import com.RTU.gourmetgamble.repositories.ProductRepository;
//import com.RTU.gourmetgamble.repositories.RecipeProductRepository;
//import com.RTU.gourmetgamble.services.*;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//
//import java.util.List;
//
//@Controller
//@RequiredArgsConstructor
//public class GenerateTableController {
//    private final RecipeService recipeService;
//    private final ProductRepository productRepository;
//    private final ProductServices productServices;
//    private final UserService userService;
//    private final AuthenticationService authenticationService;
//    private final RecipeProductRepository recipeProductRepository;
//    private final RandomServices randomServices;
//    private final ProductAPI p = new ProductAPI();
//    private final RecipeAPI r;
//    @GetMapping("/generate/products")
//    public String products(Model model) {
//        List<Product> productList = p.generateProductFromAPI();
//        p.printProductSet(productList);
//        productServices.saveProductListToDatabase(productList);
//        return "index";
//    }
//
//    @GetMapping("/generate/recipes")
//    public String recipes(Model model) {
//        r.generateRecipesFromAPIAllLetters();
//        return "index";
//    }
//}
