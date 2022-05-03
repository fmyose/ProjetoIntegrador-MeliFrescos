package br.com.meli.PIFrescos.controller.forms;

import br.com.meli.PIFrescos.models.RecipeIngredient;
import br.com.meli.PIFrescos.models.Product;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class IngredientForm {

    private Integer productId;
    private Integer quantity;

    public static RecipeIngredient convert(IngredientForm ingredientForm) {
        Product product = new Product();
        product.setProductId(ingredientForm.getProductId());
        return new RecipeIngredient(null, product, ingredientForm.getQuantity());
    }

    public static List<RecipeIngredient> convert(List<IngredientForm> ingredientForms) {
        if (ingredientForms.size() == 0) {
            throw new RuntimeException("No ingredient items in list.");
        }
        return ingredientForms.stream().map(ingredientForm -> IngredientForm.convert(ingredientForm))
                .collect(Collectors.toList());
    }
}
