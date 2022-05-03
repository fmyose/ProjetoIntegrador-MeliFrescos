package br.com.meli.PIFrescos.controller.forms;

import br.com.meli.PIFrescos.models.RecipeIngredient;
import br.com.meli.PIFrescos.models.Recipe;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RecipeForm {

    private String name;
    private List<IngredientDTO> ingredients;

    public static Recipe convert(RecipeForm recipeForm) {
        List<RecipeIngredient> ingredients = IngredientDTO.convert(recipeForm.getIngredients());
        return new Recipe(null, recipeForm.getName(), ingredients);
    }
}
