package br.com.meli.PIFrescos.service.interfaces;

import br.com.meli.PIFrescos.models.PurchaseOrder;
import br.com.meli.PIFrescos.models.RecipePurchaseOrder;
import br.com.meli.PIFrescos.models.User;

import java.math.BigDecimal;
import java.util.List;

public interface IRecipePurchaseOrderService {

    RecipePurchaseOrder purchase(String recipeName, User userLogged);

    BigDecimal calculateTotalPrice(PurchaseOrder purchaseOrder);

    List<RecipePurchaseOrder> getOpenedOrder();
}
