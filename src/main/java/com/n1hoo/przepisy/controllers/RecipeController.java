package com.n1hoo.przepisy.controllers;

import com.n1hoo.przepisy.model.Recipe;
import com.n1hoo.przepisy.service.RecipeService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {
    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    // Dodawanie nowego przepisu
    @PostMapping("/add")
    public ResponseEntity<String> addRecipe(@RequestBody Recipe recipe, HttpSession session) {
        String username = (String) session.getAttribute("user");
        if (username == null) {
            return ResponseEntity.status(401).body("🚫 Nie jesteś zalogowany!");
        }

        recipe.setAuthor(username);
        recipeService.addRecipe(recipe);
        return ResponseEntity.ok("✅ Przepis dodany!");
    }
    // Wyświetlanie przepisu
    @GetMapping("/view/{id}")
    public ResponseEntity<Recipe> viewRecipe(@PathVariable String id) {
        Recipe recipe = recipeService.viewRecipe(id);
        if (recipe != null) {
            return ResponseEntity.ok(recipe);
        }
        return ResponseEntity.notFound().build();
    }

    // Wyszukiwanie przepisów po tytule i składnikach
    @GetMapping("/search")
    public ResponseEntity<List<Recipe>> searchRecipes(@RequestParam String query) {
        List<Recipe> recipesByTitle = recipeService.searchByTitle(query);
        List<Recipe> recipesByIngredient = recipeService.searchByIngredient(query);
        recipesByIngredient.removeAll(recipesByTitle);
        recipesByTitle.addAll(recipesByIngredient);
        return ResponseEntity.ok(recipesByTitle);
    }

    // Pobieranie przepisów użytkownika
    @GetMapping("/my")
    public ResponseEntity<List<Recipe>> getUserRecipes(HttpSession session) {
        String username = (String) session.getAttribute("user");
        if (username == null) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(recipeService.getRecipesByAuthor(username));
    }

    @GetMapping("/popular")
    public ResponseEntity<List<Recipe>> getPopularRecipes() {
        return ResponseEntity.ok(recipeService.getPopularRecipes());
    }

    // Usuwanie przepisu (tylko swojego!)
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteRecipe(@PathVariable String id, HttpSession session) {
        String username = (String) session.getAttribute("user");
        if (username == null) {
            return ResponseEntity.status(401).body("🚫 Nie jesteś zalogowany!");
        }

        Optional<Recipe> recipe = recipeService.getRecipeById(id);
        if (recipe.isEmpty() || !recipe.get().getAuthor().equals(username)) {
            return ResponseEntity.status(403).body("🚫 Nie możesz usunąć tego przepisu!");
        }

        recipeService.deleteRecipe(id);
        return ResponseEntity.ok("✅ Przepis usunięty!");
    }
}