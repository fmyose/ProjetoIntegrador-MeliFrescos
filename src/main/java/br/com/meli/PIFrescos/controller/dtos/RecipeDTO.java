package br.com.meli.PIFrescos.controller.dtos;

import br.com.meli.PIFrescos.controller.forms.IngredientForm;
import br.com.meli.PIFrescos.models.Recipe;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecipeDTO {

    private String name;
    private List<IngredientForm> ingredients;

    public static RecipeDTO convert(Recipe recipe) {
        return new RecipeDTO(recipe.getName(), IngredientForm.convertToDTO(recipe.getIngredients()));
    }

    public static List<RecipeDTO> convert(List<Recipe> recipes) {
        return recipes.stream().map(recipe -> convert(recipe)).collect(Collectors.toList());
    }
}
