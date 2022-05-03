package br.com.meli.PIFrescos.service.interfaces;

import br.com.meli.PIFrescos.models.Recipe;

import java.util.List;

public interface IRecipeService {

    /**
     * get all recipesd available
     * @return List<Recipe>
     */
    List<Recipe> getAll();

    Recipe save(Recipe recipe);
    Recipe update(Integer id, Recipe recipe);
    void remove(Recipe recipe);
    void removeById(Integer id);

    Recipe findById(Integer recipeId);
}
