package br.com.meli.PIFrescos.controller.dtos;

import br.com.meli.PIFrescos.controller.forms.IngredientForm;
import br.com.meli.PIFrescos.models.RecipePurchaseOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecipePurchaseOrderDTO {

   private String recipeName;
   private BigDecimal totalPrice;

   private List<IngredientForm> ingredients;


   public static RecipePurchaseOrderDTO convert(RecipePurchaseOrder recipePurchaseOrder) {
      return RecipePurchaseOrderDTO.builder()
              .recipeName(recipePurchaseOrder.getRecipe().getName())
              .ingredients(IngredientForm.convertToDTO(recipePurchaseOrder.getRecipe().getIngredients()))
              .totalPrice(recipePurchaseOrder.getTotalPrice())
              .build();
   }
}
