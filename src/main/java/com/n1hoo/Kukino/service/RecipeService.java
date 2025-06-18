package com.n1hoo.Kukino.service;

import com.n1hoo.Kukino.model.Recipe;
import com.n1hoo.Kukino.repository.RecipeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class RecipeService {
    private static final Logger logger = LoggerFactory.getLogger(RecipeService.class);
    private final RecipeRepository recipeRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    public RecipeService(RecipeRepository recipeRepository, RedisTemplate<String, Object> redisTemplate) {
        this.recipeRepository = recipeRepository;
        this.redisTemplate = redisTemplate;
    }

    // 🟢 Wyświetlanie przepisu + aktualizacja popularności + czyszczenie cache Redis
    public Recipe viewRecipe(String id) {
        logger.debug("🔍 Wywołano viewRecipe z ID: {}", id);
        Optional<Recipe> optionalRecipe = recipeRepository.findById(id);
        
        if (optionalRecipe.isPresent()) {
            Recipe recipe = optionalRecipe.get();
            recipe.setPopularity(recipe.getPopularity() + 1);
            Recipe updatedRecipe = recipeRepository.save(recipe);

            // Po aktualizacji popularności czyszczę cache Redis, aby go odświeżyć
            redisTemplate.delete("popular_recipes");
            logger.info("🗑️ Cache 'popular_recipes' usunięty po aktualizacji popularności!");

            return updatedRecipe;
        } else {
            logger.warn("❌ Nie znaleziono przepisu o ID: {}", id);
        }
        return null;
    }

    // 🟢 Pobieranie popularnych przepisów z Redis lub MongoDB
    @SuppressWarnings("unchecked")
    public List<Recipe> getPopularRecipes() {
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();

        // Sprawdzenie, czy dane są już w Redis
        if (redisTemplate.hasKey("popular_recipes")) {
            logger.info("✅ Pobieranie popularnych przepisów z Redis!");
            return (List<Recipe>) ops.get("popular_recipes");
        }

        // Jeśli nie ma w Redis, pobieram z MongoDB
        logger.info("📡 Pobieranie popularnych przepisów z MongoDB!");
        List<Recipe> recipes = recipeRepository.findTop10ByOrderByPopularityDesc();

        // Zapis do Redis na 10 minut
        ops.set("popular_recipes", recipes, 600, TimeUnit.SECONDS);
        return recipes;
    }

    // 🟢 Dodawanie nowego przepisu + czyszczenie cache "popular_recipes"
    @CacheEvict(value = "popularRecipes", allEntries = true)
    public Recipe addRecipe(Recipe recipe) {
        logger.info("📌 Dodawanie nowego przepisu: {}", recipe.getTitle());
        Recipe savedRecipe = recipeRepository.save(recipe);

        // Czyszczenie cache po dodaniu nowego przepisu
        redisTemplate.delete("popular_recipes");
        logger.info("🗑️ Cache 'popular_recipes' usunięty po dodaniu nowego przepisu!");

        return savedRecipe;
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

    // 🟢 Usuwanie przepisu + czyszczenie cache "popular_recipes"
    public void deleteRecipe(String id) {
        logger.warn("🗑️ Usuwanie przepisu o ID: {}", id);
        recipeRepository.deleteById(id);

        // Czyszczenie cache po usunięciu przepisu
        redisTemplate.delete("popular_recipes");
        logger.info("🗑️ Cache 'popular_recipes' usunięty po usunięciu przepisu!");
    }
}