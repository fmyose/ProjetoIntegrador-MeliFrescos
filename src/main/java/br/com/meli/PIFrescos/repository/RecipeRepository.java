package br.com.meli.PIFrescos.repository;

import br.com.meli.PIFrescos.models.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecipeRepository extends JpaRepository<Recipe, Integer> {
    Optional<Recipe> findByName(String recipeName);
}
