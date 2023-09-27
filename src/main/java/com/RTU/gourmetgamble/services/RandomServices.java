package com.RTU.gourmetgamble.services;

import com.RTU.gourmetgamble.models.Recipe;
import com.RTU.gourmetgamble.repositories.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class RandomServices {

    private final RecipeRepository recipeRepository;

    public Recipe getRandomReceipt(){
        List<Recipe> allObjects = recipeRepository.findAll();
        return allObjects.get(new Random().nextInt(allObjects.size()));
    }

    public Recipe getRandomRecipeFromAList(List<Recipe> allObjects){
        return allObjects.get(new Random().nextInt(allObjects.size()));
    }
}
