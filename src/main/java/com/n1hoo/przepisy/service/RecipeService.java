package com.n1hoo.przepisy.service;

import com.n1hoo.przepisy.model.Recipe;
import com.n1hoo.przepisy.repository.RecipeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecipeService {
    private final RecipeRepository recipeRepository;

    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public Recipe addRecipe(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    public List<Recipe> searchByTitle(String title) {
        return recipeRepository.findByTitleContainingIgnoreCase(title);
    }

    public List<Recipe> searchByIngredient(String ingredient) {
        return recipeRepository.findByIngredientsContainingIgnoreCase(ingredient);
    }

    // Inne metody serwisowe
}
