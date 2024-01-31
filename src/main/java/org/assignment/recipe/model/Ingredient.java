package org.assignment.recipe.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents an Ingredient entity in the application.
 *
 * It participates in a many-to-many relationship with recipes through a join table.
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    Long id;

    @Column(unique = true)
    @JsonProperty("name")
    String name;

    @ManyToMany(mappedBy = "ingredients")
    @JsonIgnore
    private List<Recipe> recipes;
}
