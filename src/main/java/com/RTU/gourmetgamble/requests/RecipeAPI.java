package com.RTU.gourmetgamble.requests;

import com.RTU.gourmetgamble.models.Product;
import com.RTU.gourmetgamble.models.Recipe;
import com.RTU.gourmetgamble.models.RecipeProduct;
import com.RTU.gourmetgamble.repositories.ProductRepository;
import com.RTU.gourmetgamble.repositories.RecipeProductRepository;
import com.RTU.gourmetgamble.repositories.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

@Service
@RequiredArgsConstructor
public class RecipeAPI {
    private final ProductRepository productRepository;
    private final RecipeRepository recipeRepository;
    private final RecipeProductRepository recipeProductRepository;

    public void generateRecipesFromAPIAllLetters(){
        for (char letter = 'A'; letter <= 'Z'; letter++) {
            generateRecipesFromAPI(letter);
        }

    }
    public void generateRecipesFromAPI(char letter){
        List<Recipe> recipeList = new ArrayList<>();
        try {
            // Define the API URL
            String apiUrl = "https://www.themealdb.com/api/json/v1/1/search.php?f=" + letter;

            // Create an HTTP GET request to the API URL
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // Check the response code
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // Read the JSON response
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // Parse the JSON data
                JSONObject jsonResponse = new JSONObject(response.toString());

                // Extract and print values
                JSONArray meals = jsonResponse.getJSONArray("meals");
                for (int i = 0; i < meals.length(); i++) {
                    JSONObject meal = meals.getJSONObject(i);
                    String recipeName = meal.getString("strMeal");
                    String category = meal.getString("strCategory");
                    String instruction = meal.getString("strInstructions");
                    String picture = meal.getString("strMealThumb");

                    List<Product> productList = new ArrayList<>();
                    List<String> measurList = new ArrayList<>();
                    // Extract and print ingredients
                    for (int j = 1; ; j++) {
                        String ingredientKey = "strIngredient" + j;
                        String measureKey = "strMeasure" + j;

                        // Check if either the ingredient or measure key is missing
                        if (!meal.has(ingredientKey) || !meal.has(measureKey)) {
                            // If either key is missing, exit the loop
                            break;
                        }

                        String ingredient = meal.isNull(ingredientKey) ? "" : meal.getString(ingredientKey);
                        String measure = meal.isNull(measureKey) ? "" : meal.getString(measureKey);
                        // Check if either the ingredient or measure is empty
                        if (ingredient.isEmpty() || measure.isEmpty()) {
                            // If either value is empty, exit the loop
                            break;
                        }
                        Product product = productRepository.findByName(ingredient);
                        if (product != null) {
                            System.out.println(product.toString());
                            productList.add(product);
                            measurList.add(measure);
                        } else {
                            System.out.println("the product " + ingredient + " does not exsist");
                        }

//                        System.out.println("Ingredient " + j + ": " + measure + " " + ingredient);
                    }


                    Recipe recipe = new Recipe(recipeName, category, instruction, picture);
                    recipeRepository.save(recipe);
                    Long recipeId = recipe.getId(); // Get the ID of the saved recipe
                    recipe = recipeRepository.findById(recipeId).orElse(null);
                    Set<RecipeProduct> recipeProducts = new HashSet<>();
                    for (int j = 0; j < productList.size(); j++) {
                        RecipeProduct recipeProduct = new RecipeProduct();
                        recipeProduct.setRecipeId(recipe.getId());
                        recipeProduct.setProductId(productList.get(j).getId());
                        recipeProduct.setMeasure(measurList.get(j)); // Use the corresponding measure for each product

                        recipeProducts.add(recipeProduct);
                    }
                    System.out.println("_----------------------------------------");
                    System.out.println(recipe.toString());
                    System.out.println("_----------------------------------------");

//                    recipe.setRecipeProducts(recipeProducts);
                    recipeRepository.save(recipe);System.out.println(); // Add a separator between recipes
                    for (RecipeProduct rp: recipeProducts) {
                        System.out.println(rp.toString());
                        recipeProductRepository.save(rp);
                    }
                }
            } else {
                System.out.println("HTTP GET request failed with response code: " + conn.getResponseCode());
            }

            // Close the connection
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
