package br.com.meli.PIFrescos.repository;

import br.com.meli.PIFrescos.models.OrderStatus;
import br.com.meli.PIFrescos.models.RecipePurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipePurchaseOrderRepository extends JpaRepository<RecipePurchaseOrder, Integer> {

    RecipePurchaseOrder findRecipePurchaseOrderByPurchaseOrder_OrderStatus(OrderStatus orderStatus);

}
