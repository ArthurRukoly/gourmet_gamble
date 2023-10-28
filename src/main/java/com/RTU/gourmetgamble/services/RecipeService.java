package com.RTU.gourmetgamble.services;

import com.RTU.gourmetgamble.models.Product;
import com.RTU.gourmetgamble.models.Recipe;
import com.RTU.gourmetgamble.models.RecipeProduct;
import com.RTU.gourmetgamble.models.RecipeScore;
import com.RTU.gourmetgamble.repositories.RecipeProductRepository;
import com.RTU.gourmetgamble.repositories.RecipeRepository;
import com.RTU.gourmetgamble.repositories.RecipeScoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecipeService {
    private final RecipeRepository recipeRepository;
    private final RecipeProductRepository recipeProductRepository;
    private final RecipeScoreRepository recipeScoreRepository;

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
        List<List<Long>> listOfRecipesProductsLists = new ArrayList<>();
        for (Long id : productsPreferencesList) {
            List<Long> recipeIds = new ArrayList<>();
            for (RecipeProduct rp : recipeProductRepository.findRecipesByPreferences(id)) {
                recipeIds.add(rp.getRecipeId());
            }
            listOfRecipesProductsLists.add(recipeIds);
        }
        List<Long> recipeIds = findCommonNumbers(listOfRecipesProductsLists);
        for (Long id : recipeIds) {
            recipesList.add(recipeRepository.findRecipeById(id));
        }
        return recipesList;
    }

//    public Set<Recipe> getRecipeByProductNegativePreferences(List<Long> productsPreferencesList) {
//        Set<Recipe> recipesList = new HashSet<>();
//        List<List<Long>> listOfRecipesProductsLists = new ArrayList<>();
//        for (Long id : productsPreferencesList) {
//            List<Long> recipeIds = new ArrayList<>();
//            for (RecipeProduct rp : recipeProductRepository.findLimitedRecipesByNegativePreferences(id)) {
//                recipeIds.add(rp.getRecipeId());
//            }
//            listOfRecipesProductsLists.add(recipeIds);
//        }
//        List<Long> recipeIds = findCommonNumbers(listOfRecipesProductsLists);
//        for (Long id : recipeIds) {
//            recipesList.add(recipeRepository.findRecipeById(id));
//        }
//        return recipesList;
//    }

    public Set<Recipe> getRecipeByProductNegativePreferences(List<Long> productsPreferencesList) {
        Set<Recipe> recipesList = new HashSet<>();
        List<Long> recipeIds = recipeProductRepository.findLimitedRecipesByNegativePreferences(productsPreferencesList);
        for (Long id : recipeIds) {
            recipesList.add(recipeRepository.findRecipeById(id));
        }
        return recipesList;
    }
    public static List<Long> findCommonNumbers(List<List<Long>> listOfLists) {
        if (listOfLists.isEmpty()) {
            return new ArrayList<>(); // Return an empty list if the input list is empty.
        }

        // Create a list to store the common numbers.
        List<Long> commonNumbers = new ArrayList<>(listOfLists.get(0));

        // Iterate through each list in the input list.
        for (List<Long> list : listOfLists) {
            // Retain only the numbers that are common to the current list and the commonNumbers list.
            commonNumbers.retainAll(list);
        }

        return commonNumbers;
    }

    public List<Recipe> getRecipeByNegativeProductPreferences(List<Long> productsPreferencesList) {
        List<Recipe> recipesList = new ArrayList<>();
        List<List<RecipeProduct>> listOfRecipesProductsLists = new ArrayList<>();
        for (Long id : productsPreferencesList) {
            listOfRecipesProductsLists.add(recipeProductRepository.findRecipesByNegativePreferences(id));
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
            recipes = recipeRepository.findAll();
        }
        return recipes;
    }

    public List<Recipe> filterRecipesListWithBadProducts(List<Recipe> recipes, List<Long> badProductsIds){
        List<Recipe> filteredRecipes = new ArrayList<>();
        for (Recipe r: recipes) {
            List<RecipeProduct> recipeProducts = recipeProductRepository.findRelationByRecipeID(r.getId());
            boolean check = true;
            for (RecipeProduct rp: recipeProducts) {
                for (Long id: badProductsIds) {
                    if (Objects.equals(rp.getProductId(), id)){
                        check = false;
                        break;
                    }
                }
            }
            if(check){
                filteredRecipes.add(r);
            }
        }

        return filteredRecipes;
    }

    public List<Recipe> getRecipeByProductNegativePreferencesInListForm(String selectedProducts, List<Long> productsIds){
        List<Recipe> recipes = null;
        if (selectedProducts.length() > 2) {
            recipes = getRecipeByProductPreferences(productsIds);
        } else {
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

    public void setScore(Recipe recipe){
        List<RecipeScore> scoreList = recipeScoreRepository.findAllScoreOfRecipe(recipe.getId());
        System.out.println(scoreList.size());
        float scoreSum = 0;
        for (RecipeScore r: scoreList) {
            scoreSum += r.getRating();
        }
        recipe.setScore(scoreSum / scoreList.size());
    }

    public List<Recipe> limitRecipesToMaxInstances(List<Recipe> recipeList, int maxInstances) {
        if (recipeList.size() <= maxInstances) {
            return recipeList; // No need to cut the list if it's already within the limit.
        }
        List<Recipe> limitedRecipeList = new ArrayList<>();
        for (int i = 0; i < maxInstances; i++) {
            limitedRecipeList.add(recipeList.get(i));
        }

        return limitedRecipeList;
    }

    public List<Recipe> transferSetToAList(Set<Recipe> set){
        return new ArrayList<>(set);
    }
}
