package com.RTU.gourmetgamble.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@RequiredArgsConstructor
@ToString
@Entity
public class Product {
    @Id
    private Long id;
    private String name;

    public Product(Long id, String name) {
        this.id = id;
        this.name = name;
    }
//
//    @OneToMany(mappedBy = "product")
//    private Set<RecipeProduct> recipeProducts = new HashSet<>();
}