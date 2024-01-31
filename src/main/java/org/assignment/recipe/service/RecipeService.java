package org.assignment.recipe.service;

import org.assignment.recipe.model.Recipe;

import java.util.List;

/**
 * Service interface for managing Recipe entities.
 * The service contains business logic and data manipulation.
 */
public interface RecipeService {

    /**
     * Retrieve a list of all recipes.
     *
     * @return List of Recipes
     */
    List<Recipe> getAllRecipes();

    /**
     * Retrieve a recipe by its id.
     *
     * @param id The id of the recipe
     * @return Recipe
     */
    Recipe getRecipe(Long id);

    /**
     * Create a new recipe.
     *
     * @param recipe The new recipe
     * @return The created Recipe
     */
    Recipe createRecipe(Recipe recipe);

    /**
     * Update an existing recipe by its ID.
     *
     * @param id     The id of the recipe
     * @param recipe The updated Recipe object
     */
    void updateRecipe(Long id, Recipe recipe);

    /**
     * Delete a recipe by its ID.
     *
     * @param id The recipe id
     */
    void deleteRecipe(Long id);

    /**
     * Search recipes based on various criteria.
     *
     * @param vegetarian           Filter by vegetarian status
     * @param servingNumber        Filter by serving number
     * @param ingredientName       Filter by ingredient name
     * @param excludeIngredientName Exclude recipes with a specific ingredient
     * @param instructionKeyWord   Search by keyword in recipe instructions
     * @return List of recipes
     */
    List<Recipe> searchRecipe(Boolean vegetarian, Integer servingNumber,
                              String ingredientName, String excludeIngredientName,
                              String instructionKeyWord);
}
