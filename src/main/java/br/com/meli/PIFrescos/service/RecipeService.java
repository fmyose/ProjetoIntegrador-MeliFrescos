package br.com.meli.PIFrescos.service;

import br.com.meli.PIFrescos.models.Recipe;
import br.com.meli.PIFrescos.repository.RecipeRepository;
import br.com.meli.PIFrescos.service.interfaces.IRecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecipeService implements IRecipeService {

    @Autowired
    RecipeRepository recipeRepository;

    @Override
    public List<Recipe> getAll() {
        return recipeRepository.findAll();
    }

    @Override
    public Recipe save(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    @Override
    public Recipe update(Integer id, Recipe recipe) {
        Recipe recipeToUpdate = findById(id);
        recipeToUpdate.setName(recipe.getName());
        recipeToUpdate.setIngredients(recipe.getIngredients());
        return save(recipeToUpdate);
    }

    @Override
    public void remove(Recipe recipe) {
        recipeRepository.delete(recipe);
    }

    @Override
    public void removeById(Integer id) {
        recipeRepository.deleteById(id);
    }

    @Override
    public Recipe findById(Integer recipeId) {
        Optional<Recipe> recipe = recipeRepository.findById(recipeId);
        if (!recipe.isPresent()) {
            throw new RuntimeException("Recipe not found.");
        }
        return recipe.get();
    }

}
