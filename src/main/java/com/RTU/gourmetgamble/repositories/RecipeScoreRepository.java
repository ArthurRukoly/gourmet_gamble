package com.RTU.gourmetgamble.repositories;

import com.RTU.gourmetgamble.models.Recipe;
import com.RTU.gourmetgamble.models.RecipeScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeScoreRepository extends JpaRepository<RecipeScore, Long> {
    @Query("SELECT r FROM RecipeScore r WHERE r.recipeId = :recipeID")
    List<RecipeScore> findAllScoreOfRecipe(@Param("recipeID") Long recipeID);

}
