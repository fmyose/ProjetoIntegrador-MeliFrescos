package br.com.meli.PIFrescos.controller.forms;

import br.com.meli.PIFrescos.models.RecipeIngredient;
import br.com.meli.PIFrescos.models.Product;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class IngredientDTO {

    private Integer productId;
    private Integer quantity;

    public static RecipeIngredient convert(IngredientDTO ingredientDTO) {
        Product product = new Product();
        product.setProductId(ingredientDTO.getProductId());
        return new RecipeIngredient(null, product, ingredientDTO.getQuantity());
    }

    public static List<RecipeIngredient> convert(List<IngredientDTO> ingredientDTOS) {
        if (ingredientDTOS.size() == 0) {
            throw new RuntimeException("No ingredient items in list.");
        }
        return ingredientDTOS.stream().map(ingredientDTO -> IngredientDTO.convert(ingredientDTO))
                .collect(Collectors.toList());
    }
}
