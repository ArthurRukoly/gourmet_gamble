package com.RTU.gourmetgamble.models;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import jakarta.persistence.*;

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

    @Column(columnDefinition="TEXT")
    private String instruction;
    private String pictureLink;

    private float score;

//    @OneToMany(mappedBy = "recipe")
//    private Set<RecipeProduct> recipeProducts = new HashSet<>();

    public Recipe(String recipeName, String category, String instruction, String picture) {
        this.name = recipeName;
        this.category = category;
        this.instruction = instruction;
        this.pictureLink = picture;
        this.score = 0;
    }
}
