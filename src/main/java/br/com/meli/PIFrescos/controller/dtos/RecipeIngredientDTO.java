package br.com.meli.PIFrescos.controller.dtos;

import br.com.meli.PIFrescos.models.RecipeIngredient;
import br.com.meli.PIFrescos.models.Product;
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
public class RecipeIngredientDTO {

    private String name;
    private Integer quantity;


    public static RecipeIngredientDTO convertToDTO(RecipeIngredient ingredient) {
        return new RecipeIngredientDTO(ingredient.getProduct().getProductName(), ingredient.getQuantity());
    }

    public static List<RecipeIngredientDTO> convertToDTO(List<RecipeIngredient> ingredients) {
        return ingredients.stream().map(ingredient -> RecipeIngredientDTO.convertToDTO(ingredient))
                .collect(Collectors.toList());
    }
}
