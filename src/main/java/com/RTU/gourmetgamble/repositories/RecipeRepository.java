package com.RTU.gourmetgamble.repositories;

import com.RTU.gourmetgamble.models.Recipe;
import com.RTU.gourmetgamble.models.RecipeProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    @Query("SELECT r FROM Recipe r WHERE r.category = :category")
    List<Recipe> findRecipesByCategory(@Param("category") String category);

    @Query("SELECT r FROM Recipe r ORDER BY RANDOM() LIMIT 10")
    List<Recipe> findRandomRecipes();

    Recipe findRecipeById(Long id);

}
