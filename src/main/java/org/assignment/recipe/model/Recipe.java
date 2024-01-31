package org.assignment.recipe.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Represents a Recipe entity in the application.
 *
 * The class is annotated with JPA annotations for persistence in the database.
 * The many-to-many relationship with ingredients is managed through a join table.
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Long id;

    @Column(unique = true)
    @JsonProperty("name")
    private String name;

    @JsonProperty("vegetarian")
    private boolean vegetarian;

    @JsonProperty("servingNumber")
    private Integer servingNumber;

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name = "recipe_ingredient",
            joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "ingredient_id"))
    @JsonProperty("ingredients")
    private List<Ingredient> ingredients;

    @JsonProperty("instruction")
    private String instruction;
}
