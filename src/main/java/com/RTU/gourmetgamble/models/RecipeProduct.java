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
public class RecipeProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String measure;

    private Long recipeId;
    private Long productId;
}
