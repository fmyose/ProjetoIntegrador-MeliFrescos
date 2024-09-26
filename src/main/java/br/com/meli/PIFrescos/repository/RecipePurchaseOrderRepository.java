package br.com.meli.PIFrescos.repository;

import br.com.meli.PIFrescos.models.OrderStatus;
import br.com.meli.PIFrescos.models.RecipePurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecipePurchaseOrderRepository extends JpaRepository<RecipePurchaseOrder, Integer> {

    List<RecipePurchaseOrder> findRecipePurchaseOrderByPurchaseOrder_OrderStatus(OrderStatus orderStatus);

}
