package org.assignment.recipe;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.assignment.recipe.model.Recipe;
import org.assignment.recipe.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Recipe Controller is managing Recipe entities.
 * provides endpoints for all CRUD operations and search of recipes based on
 * specific criteria.
 */
@RestController
@RequestMapping("/recipes")
@Tag(name = "Recipe API", description = "Recipe operations")
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    @GetMapping
    @Operation(summary = "Get all recipes", operationId = "getRecipes")
    public ResponseEntity<List<Recipe>>  getRecipes() {
        return ResponseEntity.ok(recipeService.getAllRecipes());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Retrieve a specific recipe by ID", operationId = "getRecipe")
    public ResponseEntity<Recipe> getRecipe(@PathVariable Long id) {
        return ResponseEntity.ok(recipeService.getRecipe(id));
    }

    @PostMapping
    @Operation(summary = "Create a new recipe", operationId = "createRecipe")
    public ResponseEntity<Recipe> createRecipe(@RequestBody Recipe recipe) {
       return ResponseEntity.ok(recipeService.createRecipe(recipe));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing recipe", operationId = "updateRecipe")
    public ResponseEntity<?> updateRecipe(@PathVariable Long id,
                             @RequestBody Recipe recipe) {
        recipeService.updateRecipe(id, recipe);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a recipe", operationId = "deleteRecipe")
    public ResponseEntity<?> deleteRecipe(@PathVariable Long id) {
        recipeService.deleteRecipe(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    @Operation(summary = "Perform a filtered search for recipes", operationId = "searchRecipe")
    public List<Recipe> searchRecipe(@RequestParam(required = false) Boolean vegetarian,
                                     @RequestParam(required = false) Integer servingNumber,
                                     @RequestParam(required = false) String ingredientName,
                                     @RequestParam(required = false) String excludeIngredientName,
                                     @RequestParam(required = false) String instructionKeyWord) {
        return recipeService.searchRecipe(vegetarian, servingNumber, ingredientName, excludeIngredientName, instructionKeyWord);
    }
}
