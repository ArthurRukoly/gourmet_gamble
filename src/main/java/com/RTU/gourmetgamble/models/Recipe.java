package com.RTU.gourmetgamble.models;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@RequiredArgsConstructor
@ToString
@Entity
@Table(name = "\"receipt\"")
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String category;
    @Lob
    private String instruction;
    private String pictureLink;

    private int ratingCount;
    private float rating;

//    @OneToMany(mappedBy = "recipe")
//    private Set<RecipeProduct> recipeProducts = new HashSet<>();
    public float getScore() {
        if (ratingCount > 0){
            float score = rating / ratingCount;
            System.out.println(score);
            return (float) (Math.round(score * 10.0) / 10.0);}
        else {
            return 0;
        }
    }

    public Recipe(String recipeName, String category, String instruction, String picture) {
        this.name = recipeName;
        this.category = category;
        this.instruction = instruction;
        this.pictureLink = picture;
        this.ratingCount = 0;
        this.rating = 0;
    }
}
