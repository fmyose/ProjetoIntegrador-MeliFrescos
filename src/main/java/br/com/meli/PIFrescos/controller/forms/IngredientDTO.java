package br.com.meli.PIFrescos.controller.forms;

import br.com.meli.PIFrescos.models.Ingredient;
import br.com.meli.PIFrescos.models.Product;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class IngredientDTO {

    private Integer productId;
    private Integer quantity;

    public static Ingredient convert(IngredientDTO ingredientDTO) {
        Product product = new Product();
        product.setProductId(ingredientDTO.getProductId());
        return new Ingredient(null, product, ingredientDTO.getQuantity());
    }

    public static List<Ingredient> convert(List<IngredientDTO> ingredientDTOS) {
        if (ingredientDTOS.size() == 0) {
            throw new RuntimeException("No ingredient items in list.");
        }
        return ingredientDTOS.stream().map(ingredientDTO -> IngredientDTO.convert(ingredientDTO))
                .collect(Collectors.toList());
    }
}
