package org.assignment.recipe.service;


import org.assignment.recipe.model.Ingredient;
import org.assignment.recipe.model.Recipe;
import org.assignment.recipe.repository.IngredientRepository;
import org.assignment.recipe.repository.RecipeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RecipeServiceUnitTest {

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private IngredientRepository ingredientRepository;

    @InjectMocks
    private RecipeService recipeService= new RecipeServiceImpl();

    @Test
    public void testGetAllRecipes() {
        // Mocking repository behavior
        when(recipeRepository.findAll()).thenReturn(Arrays.asList(new Recipe(), new Recipe()));

        // Calling the service method
        List<Recipe> recipes = recipeService.getAllRecipes();

        // Verifying that the repository method was called
        verify(recipeRepository, times(1)).findAll();

        // Asserting the result
        assertEquals(2, recipes.size());
    }

    @Test
    public void testGetRecipeById() {
        // Mocking repository behavior
        Recipe expectedRecipe = new Recipe();  // Create a sample Recipe
        expectedRecipe.setId(1L);
        when(recipeRepository.findById(1L)).thenReturn(Optional.of(expectedRecipe));

        // Calling the service method
        Recipe actualRecipe = recipeService.getRecipe(1L);

        // Verifying that the repository method was called
        verify(recipeRepository, times(1)).findById(1L);

        // Asserting the result
        assertNotNull(actualRecipe);  // Ensure that a recipe is present
        assertEquals(expectedRecipe.getId(), actualRecipe.getId());  // Ensure that the expected and actual recipes match
    }

    @Test
    public void testFindByCriteria() {
        // Mocking repository behavior
       when(recipeRepository.findByCriteria(anyBoolean(), anyInt(), anyString(), anyString(), anyString()))
              .thenReturn(Arrays.asList(new Recipe(), new Recipe()));

        // Calling the search method
        List<Recipe> recipes = recipeService.searchRecipe(false, 0, "", "", "");

        // Verifying that the repository method was called
        verify(recipeRepository, times(1)).findByCriteria(false, 0, "", "", "");

        // Asserting the result
        assertEquals(2, recipes.size());
    }

    @Test
    public void testCreateRecipe() {
        // Mocking dependencies
        Recipe inputRecipe = new Recipe();
        inputRecipe.setId(100L);

        List<Ingredient> inputIngredients = new ArrayList<>();
        Ingredient ingredient1 = new Ingredient();
        ingredient1.setName("ingredient1");
        inputIngredients.add(ingredient1);
        inputRecipe.setIngredients(inputIngredients);

        when(ingredientRepository.findByName(eq("ingredient1"))).thenReturn(Optional.of(ingredient1));
        when(recipeRepository.save(any())).thenReturn(inputRecipe);

        // Calling the create method
        Recipe createdRecipe = recipeService.createRecipe(inputRecipe);

        verify(ingredientRepository, times(1)).findByName(eq("ingredient1"));
        verify(recipeRepository, times(1)).save(any());

        assertNotNull(createdRecipe.getId());
        assertNotNull(createdRecipe.getIngredients());
        assertEquals(inputIngredients.size(), createdRecipe.getIngredients().size());
    }

    @Test
    public void testUpdateRecipe() {
        // Mocking dependencies
        Long existingRecipeId = 1L;
        Recipe existingRecipe = new Recipe();
        existingRecipe.setId(existingRecipeId);
        existingRecipe.setName("Existing Recipe");
        existingRecipe.setServingNumber(2);
        existingRecipe.setInstruction("Existing Instruction");

        Recipe updatedRecipe = new Recipe();
        updatedRecipe.setId(existingRecipeId);
        updatedRecipe.setName("Updated Recipe");
        updatedRecipe.setServingNumber(4);
        updatedRecipe.setInstruction("Updated Instruction");

        List<Ingredient> inputIngredients = new ArrayList<>();
        Ingredient ingredient1 = new Ingredient();
        ingredient1.setName("ingredient1");
        inputIngredients.add(ingredient1);

        existingRecipe.setIngredients(inputIngredients);
        updatedRecipe.setIngredients(inputIngredients);

        when(recipeRepository.findById(existingRecipeId)).thenReturn(Optional.of(existingRecipe));
        when(ingredientRepository.findByName(eq("ingredient1"))).thenReturn(Optional.of(ingredient1));
        when(recipeRepository.save(any())).thenReturn(updatedRecipe);

        // Calling the service method
        recipeService.updateRecipe(existingRecipeId, updatedRecipe);


        // Verifying that following methods was called
        verify(recipeRepository, times(1)).findById(existingRecipeId);
        verify(ingredientRepository, times(1)).findByName(eq("ingredient1"));
        verify(recipeRepository, times(1)).save(any(Recipe.class));

        // Asserting the result
        assertEquals("Updated Recipe", existingRecipe.getName());
        assertEquals(4, existingRecipe.getServingNumber());
        assertEquals("Updated Instruction", existingRecipe.getInstruction());
    }

    @Test
    public void testDeleteRecipe() {
        Long existingRecipeId = 1L;
        Recipe existingRecipe = new Recipe();
        existingRecipe.setId(existingRecipeId);
        existingRecipe.setName("Existing Recipe");
        existingRecipe.setServingNumber(2);
        existingRecipe.setInstruction("Existing Instruction");

        when(recipeRepository.findById(existingRecipeId)).thenReturn(Optional.of(existingRecipe));

        // Calling the service method
        recipeService.deleteRecipe(existingRecipeId);

        // Verifying that the findById method of recipeRepository was called
        verify(recipeRepository, times(1)).findById(existingRecipeId);

        // Verifying that the deleteById method of recipeRepository was called with the correct ID
        verify(recipeRepository, times(1)).deleteById(existingRecipeId);
    }

}
