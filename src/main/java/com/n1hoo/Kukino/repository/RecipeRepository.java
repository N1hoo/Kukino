package com.n1hoo.Kukino.repository;

import com.n1hoo.Kukino.model.Recipe;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface RecipeRepository extends MongoRepository<Recipe, String> {
    List<Recipe> findByTitleContainingIgnoreCase(String title);
    List<Recipe> findByIngredientsContainingIgnoreCase(String ingredient);
    List<Recipe> findByAuthor(String author);
    List<Recipe> findTop10ByOrderByPopularityDesc();
}