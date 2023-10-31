package com.RTU.gourmetgamble.controllers;

import com.RTU.gourmetgamble.models.Product;
import com.RTU.gourmetgamble.models.Recipe;
import com.RTU.gourmetgamble.models.RecipeProduct;
import com.RTU.gourmetgamble.repositories.ProductRepository;
import com.RTU.gourmetgamble.repositories.RecipeProductRepository;
import com.RTU.gourmetgamble.repositories.RecipeRepository;
import com.RTU.gourmetgamble.requests.ProductAPI;
import com.RTU.gourmetgamble.requests.RecipeAPI;
import com.RTU.gourmetgamble.services.*;
import org.springframework.core.SpringVersion;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.Banner;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.awt.print.Pageable;
import java.util.*;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final RecipeService recipeService;
    private final ProductRepository productRepository;
    private final ProductServices productServices;
    private  final UserService userService;
    private final AuthenticationService authenticationService;
    private final RecipeProductRepository recipeProductRepository;
    private final RecipeRepository recipeRepository;
    private final RandomServices randomServices;
//    private final ProductAPI p = new ProductAPI();
//    private final RecipeAPI r;
//    private final ProductServices productServices;

    @GetMapping("/main")
    public String home(Model model) {
        List<Product> productList = productRepository.getAllProducts();
        Boolean isAuthorized = authenticationService.checkIfUserIsAuthorized();
        model.addAttribute("productList", productList);
        model.addAttribute("isAuthorized", isAuthorized);
        return "main";
    }

//    @PostMapping("/submitSelectedProducts")
//    @ResponseBody
//    public ModelAndView submitSelectedProducts(@RequestParam("selectedProducts") String PrefSelectedProducts,
//                                               @RequestParam("foodCategory") String PrefFoodCategory,
//                                               @RequestParam("notPrefFoodCategory") String NotPrefFoodCategory,
//                                               @RequestParam("notPrefSelectedProducts") String NotPrefSelectedProducts,
//                                               RedirectAttributes redirectAttributes) {
//        ModelAndView modelAndView;
//        String[] productNames = productServices.splitStringWithCommasAndPreserveQuotes(PrefSelectedProducts);
//        List<Long> productsIds = productServices.getProductsIds(productNames);
//        List<Recipe> recipes = recipeService.getRecipeByProductPreferencesInListForm(PrefSelectedProducts, productsIds);
//        for (Recipe r: recipes) {
//            System.out.println(r.getName());
//        }
//        System.out.println("-----------");
//        String[] NotPrefproductNames = productServices.splitStringWithCommasAndPreserveQuotes(NotPrefSelectedProducts);
//        List<Long> NotPrefproductsIds = productServices.getProductsIds(NotPrefproductNames);
//        List<Recipe> NotPrefrecipes = new ArrayList<>();
//        System.out.println(!NotPrefproductNames[0].equals("[]"));
//        if (!NotPrefproductNames[0].equals("[]")) {
//            NotPrefrecipes = recipeService.getRecipeByProductPreferencesInListForm(NotPrefSelectedProducts, NotPrefproductsIds);
//            recipes.removeAll(NotPrefrecipes);
//        }
//        for (Recipe r: NotPrefrecipes) {
//            System.out.println(r.getName());
//        }
//
//
//
//        if(!Objects.equals(PrefFoodCategory, "None")){
//            recipes = recipeService.selectByCategory(recipes, PrefFoodCategory);
//        }
//
//        if (recipes.size() == 0) {
//            System.out.println("no such reciept");
//            modelAndView = new ModelAndView("redirect:/notpref");
//            return modelAndView;
//        }
//
//        List<List<Product>> allProducts = new ArrayList<>();
//        for (Recipe recipe: recipes) {allProducts.add(productServices.getProductsFromIds(recipeProductRepository.findProductIDByRecipeID(recipe.getId())));}
//
//        redirectAttributes.addFlashAttribute("ingredients", allProducts);
//        redirectAttributes.addFlashAttribute("recipes", recipes);
//        redirectAttributes.addFlashAttribute("isAuthorized", authenticationService.checkIfUserIsAuthorized());
//        modelAndView = new ModelAndView("redirect:/pref");
//        return modelAndView;
//    }

    @PostMapping("/submitSelectedProducts")
    @ResponseBody
    public ModelAndView submitSelectedProducts(@RequestParam("selectedProducts") String prefSelectedProducts,
                                               @RequestParam("foodCategory") String prefFoodCategory,
                                               @RequestParam("notPrefSelectedProducts") String notPrefSelectedProducts,
                                               RedirectAttributes redirectAttributes) {
        ModelAndView modelAndView;
        List<Recipe> recipes = new ArrayList<>();
        List<Recipe> prefSelectedProductsRecipes;
        List<Recipe> prefSelectedCategoryRecipes;
        Set<Recipe> notPrefSelectedProductsRecipes;


//      -----------------------------------------Filter Preferred Selected Products--------------------------------------

        List<String> prefSelectedProductsNames = productServices.parseStringToList(prefSelectedProducts);
        if(!prefSelectedProductsNames.get(0).isEmpty()) {
            List<Long> productsIds = productServices.getProductsIds(prefSelectedProductsNames);
            prefSelectedProductsRecipes = recipeService.getRecipeByProductPreferences(productsIds);
            recipes = prefSelectedProductsRecipes;
        }

//      -----------------------------------------Filer Preferred Category-----------------------------------------------

        if (!Objects.equals(prefFoodCategory, "None")){
            if (prefSelectedProductsNames.get(0).isEmpty()) {
                prefSelectedCategoryRecipes = recipeRepository.findRecipesByCategory(prefFoodCategory);
                recipes = prefSelectedCategoryRecipes;
            } else {
                recipes = recipeService.selectByCategory(recipes, prefFoodCategory);
            }
        }

//      -----------------------------------------Filter Not Preferred Selected Products--------------------------------------

        List<String> notPrefFoodCategoryNames = productServices.parseStringToList(notPrefSelectedProducts);
        if (prefSelectedProductsNames.get(0).isEmpty() && Objects.equals(prefFoodCategory, "None")) {
            if (!notPrefFoodCategoryNames.get(0).isEmpty()) {
                List<Long> productsIds = productServices.getProductsIds(notPrefFoodCategoryNames);
                notPrefSelectedProductsRecipes = recipeService.getRecipeByProductNegativePreferences(productsIds);
                recipes = recipeService.transferSetToAList(notPrefSelectedProductsRecipes);
            }
        } else {
            if (!notPrefFoodCategoryNames.get(0).isEmpty()) {
                List<Long> productsIds = productServices.getProductsIds(notPrefFoodCategoryNames);
                recipes = recipeService.filterRecipesListWithBadProducts(recipes, productsIds);
            }
        }

//      -----------------------------------------Random Recipes if there were no filters--------------------------------

        if(prefSelectedProductsNames.get(0).isEmpty() && notPrefFoodCategoryNames.get(0).isEmpty()  && Objects.equals(prefFoodCategory, "None")){
            recipes = recipeRepository.findRandomRecipes();
        }
//      -----------------------------------------No Recipe Found Handle-------------------------------------------------

        if (recipes.isEmpty()) {
            System.out.println("no such reciept");
            modelAndView = new ModelAndView("redirect:/notpref");
            return modelAndView;
        }

//      -----------------------------------------Recipe Optimization Category-------------------------------------------
        recipes = recipeService.limitRecipesToMaxInstances(recipes, 10);

        redirectAttributes.addFlashAttribute("recipes", recipes);

        List<List<Product>> allProducts = new ArrayList<>();
        List<List<String>> allMeassures = new ArrayList<>();
        for (Recipe recipe: recipes) {allProducts.add(productServices.getProductsFromIds(recipeProductRepository.
                findProductIDByRecipeID(recipe.getId())));}
        for (Recipe recipe: recipes) {allMeassures.add(recipeProductRepository.findMeassureByRecipeID(recipe.getId()));}
        redirectAttributes.addFlashAttribute("ingredients", allProducts);
        redirectAttributes.addFlashAttribute("measures", allMeassures);
        redirectAttributes.addFlashAttribute("isAuthorized", authenticationService.checkIfUserIsAuthorized());
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
