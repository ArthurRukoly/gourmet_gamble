package com.RTU.gourmetgamble.services;

import com.RTU.gourmetgamble.models.Product;
import com.RTU.gourmetgamble.models.Recipe;
import com.RTU.gourmetgamble.models.RecipeProduct;
import com.RTU.gourmetgamble.repositories.RecipeProductRepository;
import com.RTU.gourmetgamble.repositories.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecipeService {
    private final RecipeRepository recipeRepository;
    private final RecipeProductRepository recipeProductRepository;


    public String[] recipeCategories(){
        return new String[]{
               "Dessert",
               "Chicken",
               "Beef",
               "Vegetarian",
               "Starter",
               "Miscellaneous",
               "Side",
               "Seafood",
               "Breakfast",
               "Pork",
               "Pasta",
               "Lamb",
               "Goat",
               "Vegan"
       };
    }
    public List<Recipe> getRecipeByCategory(String category){
        List<Recipe> recipeList = recipeRepository.findRecipesByCategory(category);
        return recipeList;
    }

    public List<Recipe> getRecipeByProductPreferences(List<Long> productsPreferencesList) {
        List<Recipe> recipesList = new ArrayList<>();
        List<List<RecipeProduct>> listOfRecipesProductsLists = new ArrayList<>();
        for (Long id : productsPreferencesList) {
            listOfRecipesProductsLists.add(recipeProductRepository.findRecipesByPreferences(id));
        }

        // Create a map to count occurrences of recipeIds
        Map<Long, Integer> recipeIdCountMap = new HashMap<>();

        // Iterate through each list of RecipeProduct objects
        for (List<RecipeProduct> recipeProductsList : listOfRecipesProductsLists) {
            // Create a set to store unique recipeIds in the current list
            Set<Long> uniqueRecipeIds = new HashSet<>();

            // Iterate through RecipeProduct objects in the current list
            for (RecipeProduct recipeProduct : recipeProductsList) {
                Long recipeId = recipeProduct.getRecipeId();
                uniqueRecipeIds.add(recipeId);
            }

            // Update the count of recipeIds in the map
            for (Long recipeId : uniqueRecipeIds) {
                recipeIdCountMap.put(recipeId, recipeIdCountMap.getOrDefault(recipeId, 0) + 1);
            }
        }

        // Create a list to store RecipeProduct objects with recipeIds occurring in all lists
        List<RecipeProduct> commonRecipeProducts = new ArrayList<>();

        // Iterate through each list of RecipeProduct objects again
        for (List<RecipeProduct> recipeProductsList : listOfRecipesProductsLists) {
            // Filter the RecipeProduct objects with recipeIds occurring in all lists
            List<RecipeProduct> commonProductsInList = recipeProductsList.stream()
                    .filter(recipeProduct -> recipeIdCountMap.get(recipeProduct.getRecipeId()) == listOfRecipesProductsLists.size())
                    .collect(Collectors.toList());

            // Add the common RecipeProduct objects to the commonRecipeProducts list
            commonRecipeProducts.addAll(commonProductsInList);
        }
        if(commonRecipeProducts.size() > 0) {
            for (int i = 0; i <= commonRecipeProducts.size() / 2; i++) {
                Long recipeId = commonRecipeProducts.get(i).getRecipeId();
                recipesList.add(recipeRepository.findRecipeById(recipeId));
            }
        }
        return recipesList;
    }

    public List<Recipe> getRecipeByProductPreferencesInListForm(String selectedProducts, List<Long> productsIds){
        List<Recipe> recipes = null;
        if (selectedProducts.length() > 2) {
            recipes = getRecipeByProductPreferences(productsIds);
        } else {
            System.out.println("yo");
            recipes = recipeRepository.findAll();
        }
        return recipes;
    }

    public List<Recipe> selectByCategory (List<Recipe> recipes, String category){
        List<Recipe> returnList = new ArrayList<>();
        for (Recipe r: recipes) {
            if(r.getCategory().equals(category)){
                returnList.add(r);
            }
        }
        return returnList;
    }
    public Recipe getReceiptById(Long receiptId) {
        // Use the Spring Data JPA repository to retrieve the receipt by ID
        return recipeRepository.findById(receiptId).orElse(null);
    }
}
