package br.com.meli.PIFrescos.service.interfaces;

import br.com.meli.PIFrescos.models.PurchaseOrder;
import br.com.meli.PIFrescos.models.RecipePurchaseOrder;
import br.com.meli.PIFrescos.models.User;

import java.math.BigDecimal;

public interface IPurchaseByRecipeService {

    RecipePurchaseOrder purchase(Integer recipeId, User userLogged);

    BigDecimal calculateTotalPrice(PurchaseOrder purchaseOrder);
}
