package com.n1hoo.przepisy.controllers;

import com.n1hoo.przepisy.model.Recipe;
import com.n1hoo.przepisy.service.RecipeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recipes")
@Tag(name = "Recipes", description = "Zarządzanie przepisami kulinarnymi")
public class RecipeController {

    private static final Logger logger = LoggerFactory.getLogger(RecipeController.class);

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @PostMapping
    @Operation(summary = "Dodaj nowy przepis", description = "Dodaje nowy przepis do bazy danych")
    public ResponseEntity<Recipe> addRecipe(@RequestBody Recipe recipe) {
        logger.info("Otrzymano żądanie dodania przepisu: {}", recipe.getTitle());
        Recipe savedRecipe = recipeService.addRecipe(recipe);
        return ResponseEntity.ok(savedRecipe);
    }

    @GetMapping("/search")
    @Operation(summary = "Wyszukaj przepisy", description = "Wyszukuje przepisy według nazwy lub składników")
    public ResponseEntity<List<Recipe>> searchRecipes(@RequestParam String query) {
        logger.info("Otrzymano żądanie wyszukiwania przepisów z zapytaniem: {}", query);
        List<Recipe> recipesByTitle = recipeService.searchByTitle(query);
        List<Recipe> recipesByIngredient = recipeService.searchByIngredient(query);

        // Połącz wyniki i usuń duplikaty
        recipesByIngredient.removeAll(recipesByTitle);
        recipesByTitle.addAll(recipesByIngredient);

        logger.debug("Znaleziono {} wyników dla zapytania: {}", recipesByTitle.size(), query);
        return ResponseEntity.ok(recipesByTitle);
    }

    // Inne endpointy mogą być tutaj dodane
}
