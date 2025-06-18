package com.n1hoo.Kukino.service;

import com.n1hoo.Kukino.model.Recipe;
import com.n1hoo.Kukino.repository.RecipeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecipeService {
    private static final Logger logger = LoggerFactory.getLogger(RecipeService.class);
    private final RecipeRepository recipeRepository;

    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    // 🟢 Wyświetlanie przepisu + aktualizacja popularności
    public Recipe viewRecipe(String id) {
        logger.debug("🔍 Wywołano viewRecipe z ID: {}", id);
        Optional<Recipe> optionalRecipe = recipeRepository.findById(id);

        if (optionalRecipe.isPresent()) {
            Recipe recipe = optionalRecipe.get();
            recipe.setPopularity(recipe.getPopularity() + 1);
            Recipe updatedRecipe = recipeRepository.save(recipe);
            return updatedRecipe;
        } else {
            logger.warn("❌ Nie znaleziono przepisu o ID: {}", id);
        }
        return null;
    }

    // 🟢 Pobieranie popularnych przepisów z MongoDB
    public List<Recipe> getPopularRecipes() {
        logger.info("📡 Pobieranie popularnych przepisów z MongoDB!");
        return recipeRepository.findTop10ByOrderByPopularityDesc();
    }

    // 🟢 Dodawanie nowego przepisu
    public Recipe addRecipe(Recipe recipe) {
        logger.info("📌 Dodawanie nowego przepisu: {}", recipe.getTitle());
        return recipeRepository.save(recipe);
    }

    // 🟢 Wyszukiwanie przepisów po tytule
    public List<Recipe> searchByTitle(String title) {
        logger.debug("🔍 Wyszukiwanie przepisów po tytule: {}", title);
        return recipeRepository.findByTitleContainingIgnoreCase(title);
    }

    // 🟢 Wyszukiwanie przepisów po składniku
    public List<Recipe> searchByIngredient(String ingredient) {
        logger.debug("🔍 Wyszukiwanie przepisów po składniku: {}", ingredient);
        return recipeRepository.findByIngredientsContainingIgnoreCase(ingredient);
    }

    // 🟢 Pobieranie przepisów użytkownika
    public List<Recipe> getRecipesByAuthor(String author) {
        logger.debug("📜 Pobieranie przepisów użytkownika: {}", author);
        return recipeRepository.findByAuthor(author);
    }

    // 🟢 Pobieranie pojedynczego przepisu
    public Optional<Recipe> getRecipeById(String id) {
        logger.debug("🔍 Pobieranie przepisu o ID: {}", id);
        return recipeRepository.findById(id);
    }

    // 🟢 Usuwanie przepisu
    public void deleteRecipe(String id) {
        logger.warn("🗑️ Usuwanie przepisu o ID: {}", id);
        recipeRepository.deleteById(id);
    }
}