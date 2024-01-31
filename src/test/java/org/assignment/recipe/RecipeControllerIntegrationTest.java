package org.assignment.recipe;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assignment.recipe.model.Ingredient;
import org.assignment.recipe.model.Recipe;
import org.assignment.recipe.service.RecipeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class RecipeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RecipeService recipeService;

    @Test
    public void testGetRecipes() throws Exception {
        // Mock the service response
        when(recipeService.getAllRecipes()).thenReturn(Arrays.asList(new Recipe(), new Recipe()));

        mockMvc.perform(MockMvcRequestBuilders.get("/recipes"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    public void testGetRecipe() throws Exception {
        // Create Recipe
        Recipe recipe = getCreatedRecipe();
        // Mock the service response
        when(recipeService.getRecipe(recipe.getId())).thenReturn(recipe);

        mockMvc.perform(MockMvcRequestBuilders.get("/recipes/{id}", recipe.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id").value(recipe.getId().intValue()));
    }

    @Test
    public void testCreateRecipe() throws Exception {
        // Create Recipe
        Recipe createdRecipe = getCreatedRecipe();
        // Mock the service response
        when(recipeService.createRecipe(any())).thenReturn(createdRecipe);

        mockMvc.perform(MockMvcRequestBuilders.post("/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createdRecipe)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id").value(createdRecipe.getId().intValue()))
                .andExpect(jsonPath("$.name").value(createdRecipe.getName()))
                .andExpect(jsonPath("$.instruction").value(createdRecipe.getInstruction()));
   }

    @Test
    public void testUpdateRecipe() throws Exception {
        // Create Recipe
        Recipe recipe = getCreatedRecipe();

        mockMvc.perform(MockMvcRequestBuilders.put("/recipes/{id}", recipe.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recipe)))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        verify(recipeService, times(1)).updateRecipe(eq(recipe.getId()), any(Recipe.class));
    }

    @Test
    public void testDeleteRecipe() throws Exception {
        // Set recipeId
        Long recipeId = 1L;

        mockMvc.perform(MockMvcRequestBuilders.delete("/recipes/{id}", recipeId))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        verify(recipeService, times(1)).deleteRecipe(eq(recipeId));
    }

    @Test
    public void testSearchRecipe() throws Exception {
        // Mock the service response
        when(recipeService.searchRecipe(any(), any(), any(), any(), any())).thenReturn(Arrays.asList(new Recipe(), new Recipe()));

        mockMvc.perform(MockMvcRequestBuilders.get("/recipes/search"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    private static List<Ingredient> getIngredients() {
        List<Ingredient> inputIngredients = new ArrayList<>();
        Ingredient ingredient = new Ingredient();
        ingredient.setName("New Ingredient");
        inputIngredients.add(ingredient);
        return inputIngredients;
    }
    private static Recipe getCreatedRecipe() {
        Recipe createdRecipe = new Recipe();
        createdRecipe.setId(1L);
        createdRecipe.setServingNumber(2);
        createdRecipe.setName("New Recipe");
        createdRecipe.setIngredients(getIngredients());
        createdRecipe.setInstruction("Cut ingredients and boil.");
        return createdRecipe;
    }
}
