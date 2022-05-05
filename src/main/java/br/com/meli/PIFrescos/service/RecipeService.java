package br.com.meli.PIFrescos.service;

import br.com.meli.PIFrescos.models.Product;
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

    @Autowired
    ProductService productService;

    @Override
    public List<Recipe> getAll() {
        return recipeRepository.findAll();
    }

    @Override
    public Recipe save(Recipe recipe) {
        recipe = updateRecipeIngredientsProductInfo(recipe);
        return recipeRepository.save(recipe);
    }

    /**
     * Procura os produtos que consta na lista de ingrediente e preenche os valores nulos.
     * Não é critico, apenas para que o DTO retorne a lista de ingrediente com o nome.
     * @param recipe
     * @return
     */
    public Recipe updateRecipeIngredientsProductInfo(Recipe recipe) {
        recipe.getIngredients().forEach(ingredient -> {
            Product product = productService.findProductById(ingredient.getProduct().getProductId());
            ingredient.setProduct(product);
        });
        return recipe;
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

    @Override
    public Recipe findByName(String recipeName) {
        Optional<Recipe> recipe = recipeRepository.findByName(recipeName);
        if (!recipe.isPresent()) {
            throw new RuntimeException("Recipe not found.");
        }
        return recipe.get();
    }
}
