package com.RTU.gourmetgamble.controllers;

import com.RTU.gourmetgamble.models.Product;
import com.RTU.gourmetgamble.models.Recipe;
import com.RTU.gourmetgamble.repositories.ProductRepository;
import com.RTU.gourmetgamble.repositories.RecipeProductRepository;
import com.RTU.gourmetgamble.repositories.RecipeRepository;
import com.RTU.gourmetgamble.requests.ProductAPI;
import com.RTU.gourmetgamble.requests.RecipeAPI;
import com.RTU.gourmetgamble.services.ProductServices;
import com.RTU.gourmetgamble.services.RandomServices;
import com.RTU.gourmetgamble.services.RecipeService;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.Banner;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final RecipeService recipeService;
    private final ProductRepository productRepository;
    private final ProductServices productServices;
    private final RecipeRepository recipeRepository;
    private final RecipeProductRepository recipeProductRepository;
    private final RandomServices randomServices;
//    private final ProductAPI p = new ProductAPI();
//    private final RecipeAPI r;
//    private final ProductServices productServices;

    @GetMapping("/main")
    public String home(Model model) {
//        recipeService.getRecipeByCategory("Beef");
//        List<Long> list = new ArrayList<>();
//        list.add(41L); Chicken
//        list.add(141L); Eggs plant
//        List<Recipe> recipes = recipeService.getRecipeByProductPreferences(list);
//        for (Recipe r: recipes) {
//            System.out.println(r.toString());
//        }


//--------------------------Get Products from API-----------------------------------------------------------------------
//        List<Product> productList = p.generateProductFromAPI();
//        p.printProductSet(productList);
//        productServices.saveProductListToDatabase(productList);

//        r.generateRecipesFromAPIAllLetters();

        List<Product> productList = productRepository.getAllProducts();
        model.addAttribute("productList", productList);
        return "main";
    }

    @PostMapping("/submitSelectedProducts")
    @ResponseBody
    public ModelAndView submitSelectedProducts(@RequestParam("selectedProducts") String PrefSelectedProducts,
                                               @RequestParam("foodCategory") String PrefFoodCategory,
                                               @RequestParam("notPrefFoodCategory") String NotPrefFoodCategory,
                                               @RequestParam("notPrefSelectedProducts") String NotPrefSelectedProducts,
                                               RedirectAttributes redirectAttributes) {
        ModelAndView modelAndView;
        System.out.println(PrefSelectedProducts);
        System.out.println(NotPrefSelectedProducts);
        System.out.println(NotPrefSelectedProducts.length());

        String[] productNames = productServices.splitStringWithCommasAndPreserveQuotes(PrefSelectedProducts);
        List<Long> productsIds = productServices.getProductsIds(productNames);
        List<Recipe> recipes = recipeService.getRecipeByProductPreferencesInListForm(PrefSelectedProducts, productsIds);
        for (Recipe r: recipes) {
            System.out.println(r.getName());
        }
        System.out.println("-----------");
        String[] NotPrefproductNames = productServices.splitStringWithCommasAndPreserveQuotes(NotPrefSelectedProducts);
        List<Long> NotPrefproductsIds = productServices.getProductsIds(NotPrefproductNames);
        List<Recipe> NotPrefrecipes = new ArrayList<>();
        System.out.println(!NotPrefproductNames[0].equals("[]"));
        if (!NotPrefproductNames[0].equals("[]")) {
            NotPrefrecipes = recipeService.getRecipeByProductPreferencesInListForm(NotPrefSelectedProducts, NotPrefproductsIds);
            recipes.removeAll(NotPrefrecipes);
        }
        for (Recipe r: NotPrefrecipes) {
            System.out.println(r.getName());
        }



        if(!Objects.equals(PrefFoodCategory, "None")){
            recipes = recipeService.selectByCategory(recipes, PrefFoodCategory);
        }

        if (recipes.size() == 0) {
            System.out.println("no such reciept");
            modelAndView = new ModelAndView("redirect:/notpref");
            return modelAndView;
        }

        List<List<Product>> allProducts = new ArrayList<>();
        for (Recipe recipe: recipes) {allProducts.add(productServices.getProductsFromIds(recipeProductRepository.findProductIDByRecipeID(recipe.getId())));}

        redirectAttributes.addFlashAttribute("ingredients", allProducts);
        redirectAttributes.addFlashAttribute("recipes", recipes);

        modelAndView = new ModelAndView("redirect:/pref");
        return modelAndView;
    }

    @GetMapping("/pref")
    public String pref(Model model) {
        model.getAttribute("recipes");
        model.getAttribute("ingredients");
        return "random";
    }

    @GetMapping("/notpref")
    public String notpref(Model model) {
        return "test";
    }

}
