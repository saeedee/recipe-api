package org.assignment.recipe.repository;

import org.assignment.recipe.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * RecipeRepository interface for managing Recipe in the database.
 * Extends the Spring Data JPA JpaRepository to provide CRUD operations
 * and additional query methods.
 */
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    @Query("SELECT r FROM Recipe r " +
            "WHERE (:vegetarian IS NULL OR r.vegetarian = :vegetarian) " +
            "AND (:servingNumber IS NULL OR r.servingNumber = :servingNumber) " +
            "AND (:ingredientName IS NULL OR :ingredientName IN (SELECT i.name FROM r.ingredients i)) " +
            "AND (:excludedIngredientName IS NULL OR :excludedIngredientName NOT IN (SELECT i.name FROM r.ingredients i)) " +
            "AND (:instructionKeyWord IS NULL OR r.instruction LIKE %:instructionKeyWord%)")
    List<Recipe> findByCriteria(Boolean vegetarian, Integer servingNumber, String ingredientName, String excludedIngredientName, String instructionKeyWord);
}
