package com.n1hoo.przepisy.controllers;

import com.n1hoo.przepisy.model.Recipe; // Upewnij się, że importujesz właściwy model!
import com.n1hoo.przepisy.service.RecipeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {
    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @PostMapping
    public ResponseEntity<Recipe> addRecipe(@RequestBody Recipe recipe) {
        Recipe savedRecipe = recipeService.addRecipe(recipe);
        return ResponseEntity.ok(savedRecipe);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Recipe>> searchRecipes(@RequestParam String query) {
        List<Recipe> recipesByTitle = recipeService.searchByTitle(query);
        List<Recipe> recipesByIngredient = recipeService.searchByIngredient(query);
        recipesByIngredient.removeAll(recipesByTitle);
        recipesByTitle.addAll(recipesByIngredient);
        return ResponseEntity.ok(recipesByTitle);
    }
}
