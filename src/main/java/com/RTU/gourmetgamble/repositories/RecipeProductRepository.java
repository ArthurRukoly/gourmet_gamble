package com.RTU.gourmetgamble.repositories;

import com.RTU.gourmetgamble.models.Product;
import com.RTU.gourmetgamble.models.RecipeProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeProductRepository extends JpaRepository<RecipeProduct, Long> {
    @Query("SELECT rp.productId FROM RecipeProduct rp WHERE rp.recipeId = :id")
    List<Long> findProductIDByRecipeID(@Param("id") Long id);

    @Query("SELECT rp FROM RecipeProduct rp WHERE rp.productId = :id")
    List<RecipeProduct> findRecipesByPreferences(@Param("id") Long id);

    @Query("SELECT rp FROM RecipeProduct rp WHERE rp.productId <> :id")
    List<RecipeProduct> findRecipesByNegativePreferences(@Param("id") Long id);

}