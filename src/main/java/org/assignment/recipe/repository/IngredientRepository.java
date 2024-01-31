package org.assignment.recipe.repository;

import org.assignment.recipe.model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * IngredientRepository interface for managing Ingredient entities in the database.
 * Extends the Spring Data JPA JpaRepository to provide CRUD operations.
 */
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

    Optional<Ingredient> findByName(String name);
}
