package de.whitefallen.recipes.repositories;

import de.whitefallen.recipes.domain.Recipe;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RecipeRepository extends MongoRepository<Recipe, String> {
    //Recipe findByName(String name);
}
