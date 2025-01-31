package com.n1hoo.przepisy.service;

import com.n1hoo.przepisy.model.Recipe;
import com.n1hoo.przepisy.repository.RecipeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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

    // 🔹 Dodawanie nowego przepisu
    @CacheEvict(value = "popularRecipes", allEntries = true) // Czyszczenie cache przy dodaniu nowego przepisu
    public Recipe addRecipe(Recipe recipe) {
        logger.info("📌 Dodawanie nowego przepisu: {}", recipe.getTitle());
        return recipeRepository.save(recipe);
    }

    // 🔹 Wyszukiwanie przepisów po tytule
    public List<Recipe> searchByTitle(String title) {
        logger.debug("🔍 Wyszukiwanie przepisów po tytule: {}", title);
        return recipeRepository.findByTitleContainingIgnoreCase(title);
    }

    // 🔹 Wyszukiwanie przepisów po składniku
    public List<Recipe> searchByIngredient(String ingredient) {
        logger.debug("🔍 Wyszukiwanie przepisów po składniku: {}", ingredient);
        return recipeRepository.findByIngredientsContainingIgnoreCase(ingredient);
    }

    // 🔹 Pobieranie popularnych przepisów z cache lub bazy danych
    @Cacheable(value = "popularRecipes")
    public List<Recipe> getPopularRecipes() {
        logger.info("📊 Pobieranie popularnych przepisów z bazy danych.");
        // Można dodać sortowanie według popularności
        return recipeRepository.findAll();
    }

    // 🔹 Pobieranie przepisów użytkownika
    public List<Recipe> getRecipesByAuthor(String author) {
        logger.debug("📜 Pobieranie przepisów użytkownika: {}", author);
        return recipeRepository.findByAuthor(author);
    }

    // 🔹 Pobieranie pojedynczego przepisu
    public Optional<Recipe> getRecipeById(String id) {
        logger.debug("🔍 Pobieranie przepisu o ID: {}", id);
        return recipeRepository.findById(id);
    }

    // 🔹 Usuwanie przepisu
    public void deleteRecipe(String id) {
        logger.warn("🗑️ Usuwanie przepisu o ID: {}", id);
        recipeRepository.deleteById(id);
    }
}
