package br.com.meli.PIFrescos.service;

import br.com.meli.PIFrescos.models.Batch;
import br.com.meli.PIFrescos.models.OrderStatus;
import br.com.meli.PIFrescos.models.ProductsCart;
import br.com.meli.PIFrescos.models.PurchaseOrder;
import br.com.meli.PIFrescos.models.Recipe;
import br.com.meli.PIFrescos.models.RecipePurchaseOrder;
import br.com.meli.PIFrescos.models.User;
import br.com.meli.PIFrescos.repository.RecipePurchaseOrderRepository;
import br.com.meli.PIFrescos.service.interfaces.IBatchService;
import br.com.meli.PIFrescos.service.interfaces.IPurchaseOrderService;
import br.com.meli.PIFrescos.service.interfaces.IRecipePurchaseOrderService;
import br.com.meli.PIFrescos.service.interfaces.IRecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class RecipePurchaseOrderService implements IRecipePurchaseOrderService {

    @Autowired
    RecipePurchaseOrderRepository recipePurchaseOrderRepository;

    @Autowired
    IRecipeService recipeService;

    @Autowired
    IBatchService batchService;

    @Autowired
    IPurchaseOrderService purchaseOrderService;

    @Override
    public RecipePurchaseOrder purchase(Integer recipeId, User userLogged) {

        Recipe recipe = recipeService.findById(recipeId);

        // para cada ingrediente, ver se existe um batch que satisfaz a compra
        List<ProductsCart> productsCarts = new ArrayList<>();
        recipe.getIngredients().forEach(ingredient -> {
            Integer productId = ingredient.getProduct().getProductId();
            Integer quantity = ingredient.getQuantity();
            List<Batch> batchList = batchService.findBatchesByProductIdAndCurrentQuantityGreaterThanEqual(productId, quantity);
            if (batchList.size() == 0) {
                throw new RuntimeException("Ingredient not available.");
            }
            ProductsCart productsCart = new ProductsCart();
            productsCart.setBatch(batchList.get(0));
            productsCart.setQuantity(quantity);

            productsCarts.add(productsCart);
        });

        // salvar a ordem de compra antes de salvar a compra pola receita
        PurchaseOrder purchaseOrder = PurchaseOrder.builder()
                .user(userLogged)
                .orderStatus(OrderStatus.OPENED)
                .date(LocalDate.now())
                .cartList(productsCarts)
                .build();

        BigDecimal totalPrice = calculateTotalPrice(purchaseOrder);

        purchaseOrderService.save(purchaseOrder);

        RecipePurchaseOrder recipePurchaseOrder = RecipePurchaseOrder.builder()
                .recipe(recipe)
                .purchaseOrder(purchaseOrder)
                .totalPrice(totalPrice)
                .build();

        return recipePurchaseOrderRepository.save(recipePurchaseOrder);
    }

    @Override
    public BigDecimal calculateTotalPrice(PurchaseOrder purchaseOrder) {
        return purchaseOrderService.calculateTotalPrice(purchaseOrder);
    }

    @Override
    public List<RecipePurchaseOrder> getOpenedOrder() {
        List<RecipePurchaseOrder> recipePurchaseOrder =
                recipePurchaseOrderRepository.findRecipePurchaseOrderByPurchaseOrder_OrderStatus(OrderStatus.OPENED);

        return recipePurchaseOrder;
    }

}
