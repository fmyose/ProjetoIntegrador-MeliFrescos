package br.com.meli.PIFrescos.service;

import br.com.meli.PIFrescos.models.Batch;
import br.com.meli.PIFrescos.models.OrderStatus;
import br.com.meli.PIFrescos.models.Product;
import br.com.meli.PIFrescos.models.PurchaseOrder;
import br.com.meli.PIFrescos.models.Recipe;
import br.com.meli.PIFrescos.models.RecipeIngredient;
import br.com.meli.PIFrescos.models.RecipePurchaseOrder;
import br.com.meli.PIFrescos.models.StorageType;
import br.com.meli.PIFrescos.models.User;
import br.com.meli.PIFrescos.repository.RecipePurchaseOrderRepository;
import br.com.meli.PIFrescos.service.interfaces.IBatchService;
import br.com.meli.PIFrescos.service.interfaces.IPurchaseOrderService;
import br.com.meli.PIFrescos.service.interfaces.IRecipePurchaseOrderService;
import br.com.meli.PIFrescos.service.interfaces.IRecipeService;
import org.hibernate.result.UpdateCountOutput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class RecipePurchaseOrderServiceTest {

    @Mock
    IRecipeService recipeService;

    @Mock
    IBatchService batchService;

    @Mock
    IPurchaseOrderService purchaseOrderService;

    @Mock
    RecipePurchaseOrderRepository recipePurchaseOrderRepository;

    @InjectMocks
    RecipePurchaseOrderService recipePurchaseOrderService;

    private User user = new User();
    private Product product1 = new Product(1, "product 1",StorageType.FRESH, "product 1 description");
    private Product product2 = new Product(2, "product 2",StorageType.FRESH, "product 2 description");
    private RecipeIngredient ingredient1 = new RecipeIngredient(1, product1, 1);
    private RecipeIngredient ingredient2 = new RecipeIngredient(2, product2, 2);
    private List<RecipeIngredient> ingredients = new ArrayList<>(Arrays.asList(ingredient1, ingredient2));
    private Recipe recipe1 = new Recipe(1, "recipe 1", ingredients);
    private Batch batch1 = new Batch();
    private Batch batch2 = new Batch();
    private List<Batch> batches = new ArrayList<>(Arrays.asList(batch1, batch2));
    private PurchaseOrder purchaseOrder = new PurchaseOrder();
    private RecipePurchaseOrder recipePurchaseOrder = new RecipePurchaseOrder();
    private List<RecipePurchaseOrder> recipePurchaseOrders = new ArrayList<>();

    @BeforeEach
    void setup() {
        user.setId(1);
        batch1.setUnitPrice(BigDecimal.valueOf(5));
        batch2.setUnitPrice(BigDecimal.valueOf(10));
        purchaseOrder.setOrderStatus(OrderStatus.OPENED);

        recipePurchaseOrder.setId(1);
        recipePurchaseOrder.setPurchaseOrder(purchaseOrder);
        recipePurchaseOrder.setRecipe(recipe1);
        recipePurchaseOrder.setTotalPrice(BigDecimal.valueOf(25));

        recipePurchaseOrders.add(recipePurchaseOrder);
    }


    @Test
    void recipePurchase() {
        Mockito.when(recipeService.findByName(any())).thenReturn(recipe1);
        Mockito.when(batchService.findBatchesByProductIdAndCurrentQuantityGreaterThanEqual(any(),any())).thenReturn(batches);
        Mockito.when(purchaseOrderService.save(any())).thenReturn(purchaseOrder);
        Mockito.when(recipePurchaseOrderRepository.save(any())).thenReturn(recipePurchaseOrder);

        RecipePurchaseOrder returnValue = recipePurchaseOrderService.purchase(recipe1.getName(), user);

        assertEquals(recipePurchaseOrder, returnValue);
    }

    @Test
    void tryToRecipePurchaseWithMissingIngredientInStock() {
        String expectedErrorMsg = "Ingredient not available.";
        List<Batch> emptyBatchList = new ArrayList<>();

        Mockito.when(recipeService.findByName(any())).thenReturn(recipe1);
        Mockito.when(batchService.findBatchesByProductIdAndCurrentQuantityGreaterThanEqual(any(),any())).thenReturn(emptyBatchList);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> recipePurchaseOrderService.purchase(recipe1.getName(), user));

        assertEquals(expectedErrorMsg, exception.getMessage());
    }

    @Test
    void calculateTotalPrice() {
        BigDecimal expected = BigDecimal.valueOf(10);

        Mockito.when(purchaseOrderService.calculateTotalPrice(purchaseOrder)).thenReturn(expected);

        BigDecimal returnValue = recipePurchaseOrderService.calculateTotalPrice(purchaseOrder);

        assertEquals(expected, returnValue);
    }

    @Test
    void getOpenedOrder() {
        Mockito.when(recipePurchaseOrderRepository.findRecipePurchaseOrderByPurchaseOrder_OrderStatus(OrderStatus.OPENED))
                .thenReturn(recipePurchaseOrders);

        List<RecipePurchaseOrder> returnValue = recipePurchaseOrderService.getOpenedOrder();

        assertEquals(returnValue.size(), recipePurchaseOrders.size());
    }
}