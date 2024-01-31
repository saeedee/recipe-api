package org.assignment.recipe.service;

import org.assignment.recipe.exception.NotFoundException;
import org.assignment.recipe.model.Ingredient;
import org.assignment.recipe.model.Recipe;
import org.assignment.recipe.repository.IngredientRepository;
import org.assignment.recipe.repository.RecipeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RecipeServiceImpl implements RecipeService {

    Logger logger = LoggerFactory.getLogger(RecipeServiceImpl.class);

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Override
    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }

    @Override
    public Recipe getRecipe(Long id) {
        return recipeRepository.findById(id).get();
    }

    @Override
    public Recipe createRecipe(Recipe recipe) {
        List<Ingredient> ingredients = resolveIngredients(recipe.getIngredients());
        recipe.setIngredients(ingredients);
        return recipeRepository.save(recipe);
    }

    @Override
    public void updateRecipe(Long id, Recipe updatedRecipe) {
        // found the recipe
        Optional<Recipe> recipe = recipeRepository.findById(id);

        if (recipe.isPresent()) {
            // found the existing recipe
            Recipe existingRecipe = recipe.get();
            List<Ingredient> ingredients = resolveIngredients(updatedRecipe.getIngredients());

            // Update properties
            existingRecipe.setName(updatedRecipe.getName());
            existingRecipe.setServingNumber(updatedRecipe.getServingNumber());
            existingRecipe.setInstruction(updatedRecipe.getInstruction());
            existingRecipe.setIngredients(ingredients);

            // Save the updated recipe
            recipeRepository.save(existingRecipe);
        } else {
            // throw an exception if recipe not found
            throw new NotFoundException("Recipe not found with id: " + id);
        }
    }

    @Override
    public void deleteRecipe(Long id) {
        // found the recipe
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Recipe not found with id: " + id));
        recipeRepository.deleteById(recipe.getId());
    }

    @Override
    public List<Recipe> searchRecipe(Boolean vegetarian, Integer servingNumber, String ingredientName, String excludeIngredientName, String instructionKeyWord) {
        // Log search parameter values
        logger.info("vegetarian: {}, servingNumber: {}, ingredientName: {}, excludedIngredientName: {}, instructionKeyWord: {}",
                vegetarian, servingNumber, ingredientName, excludeIngredientName, instructionKeyWord);

        return recipeRepository.findByCriteria(vegetarian, servingNumber, ingredientName, excludeIngredientName, instructionKeyWord);
    }

    /**
     * Process the ingredient to find them or save them.
     *
     * @param ingredients the list of ingredient
     * @return list of ingredients
     */
    private List<Ingredient> resolveIngredients(List<Ingredient> ingredients) {
        if (ingredients == null) {
            // throw an exception if ingredients is null
            throw new NotFoundException("Ingredients cannot be null");
        }
        return ingredients.stream()
                .map(this::resolveIngredient)
                .collect(Collectors.toList());
    }

    /**
     * Find ingredient based on its name otherwise call save.
     *
     * @param inputIngredient the ingredient
     * @return the ingredient
     */
    private Ingredient resolveIngredient(Ingredient inputIngredient) {
        return ingredientRepository.findByName(inputIngredient.getName())
                .orElseGet(() -> saveIngredient(inputIngredient.getName()));
    }

    /**
     * Save ingredient if it does not exist.
     *
     * @param ingredientName the name of ingredient
     * @return the Ingredient
     */
    private Ingredient saveIngredient(String ingredientName) {
        Ingredient newIngredient = new Ingredient();
        newIngredient.setName(ingredientName);
        return ingredientRepository.save(newIngredient);
    }

}
