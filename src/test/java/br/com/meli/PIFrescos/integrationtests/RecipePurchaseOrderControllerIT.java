package br.com.meli.PIFrescos.integrationtests;

import br.com.meli.PIFrescos.controller.dtos.TokenDto;
import br.com.meli.PIFrescos.controller.forms.IngredientForm;
import br.com.meli.PIFrescos.controller.forms.RecipeForm;
import br.com.meli.PIFrescos.models.Batch;
import br.com.meli.PIFrescos.models.OrderStatus;
import br.com.meli.PIFrescos.models.Product;
import br.com.meli.PIFrescos.models.ProductsCart;
import br.com.meli.PIFrescos.models.Profile;
import br.com.meli.PIFrescos.models.PurchaseOrder;
import br.com.meli.PIFrescos.models.Recipe;
import br.com.meli.PIFrescos.models.RecipeIngredient;
import br.com.meli.PIFrescos.models.RecipePurchaseOrder;
import br.com.meli.PIFrescos.models.StorageType;
import br.com.meli.PIFrescos.models.User;
import br.com.meli.PIFrescos.models.UserRole;
import br.com.meli.PIFrescos.repository.RecipePurchaseOrderRepository;
import br.com.meli.PIFrescos.repository.UserRepository;
import br.com.meli.PIFrescos.service.BatchServiceImpl;
import br.com.meli.PIFrescos.service.PurchaseOrderService;
import br.com.meli.PIFrescos.service.RecipeService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
public class RecipePurchaseOrderControllerIT {

    @MockBean
    private UserRepository userRepository;


    @MockBean
    private BatchServiceImpl batchService;

    @MockBean
    private RecipeService recipeService;

    @MockBean
    private PurchaseOrderService purchaseOrderService;

    @MockBean
    private RecipePurchaseOrderRepository recipePurchaseOrderRepository;


    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    String userLogin = "{"
            + "\"email\": \"meli@gmail.com\", "
            + "\"password\": \"123456\""
            + "}";

    private String accessToken;

    Profile profile = new Profile();
    User userMock = User.builder().id(1).fullname("John Doe").email("john@mercadolivre.com.br")
            .password("$2a$10$GtzVniP9dVMmVW2YxytuvOG9kHu9nrwAxe8/UXSFkaECmIJ4UJcHy")
            .profiles(List.of(profile)).role(UserRole.ADMIN)
            .build();

    private String baseUrl = "/fresh-products/recipe";

    private Product product1 = new Product(1, "product 1", StorageType.FRESH, "product 1 desc");
    private Product product2 = new Product(2, "product 2", StorageType.FRESH, "product 2 desc");
    private RecipeIngredient ingredient1 = new RecipeIngredient(1, product1, 10);
    private RecipeIngredient ingredient2 = new RecipeIngredient(2, product2, 20);
    private IngredientForm ingredientForm1 = new IngredientForm(1, 10);
    private IngredientForm ingredientForm2 = new IngredientForm(2, 20);
    private RecipeForm recipeForm1= new RecipeForm("recipe 1", new ArrayList<>(Arrays.asList(ingredientForm1, ingredientForm2)));

    private Recipe recipe1 = new Recipe(1, "recipe 1", new ArrayList<>(Arrays.asList(ingredient1, ingredient2)));
    private Recipe recipe2 = new Recipe(2, "recipe 2", new ArrayList<>());
    private List<Recipe> recipes = new ArrayList<>(Arrays.asList(recipe1, recipe2));

    private Batch batch1 = Batch.builder().batchNumber(1).currentQuantity(100).product(product1)
            .unitPrice(BigDecimal.valueOf(1)).build();
    private Batch batch2 = Batch.builder().batchNumber(2).currentQuantity(200).product(product2)
            .unitPrice(BigDecimal.valueOf(2)).build();
    private List<Batch> batchList1 = new ArrayList<>(Arrays.asList(batch1));
    private List<Batch> batchList2 = new ArrayList<>(Arrays.asList(batch2));

    private ProductsCart productsCart1 = new ProductsCart(1, batch1, 10);
    private ProductsCart productsCart2 = new ProductsCart(2, batch2, 20);
    private List<ProductsCart> cartList = new ArrayList<>(Arrays.asList(productsCart1, productsCart2));

    private PurchaseOrder purchaseOrder1 = PurchaseOrder.builder().id(1).orderStatus(OrderStatus.OPENED)
            .cartList(cartList).user(userMock)
            .build();

    private RecipePurchaseOrder recipePurchaseOrder1 = RecipePurchaseOrder.builder().id(1)
            .purchaseOrder(purchaseOrder1).recipe(recipe1).totalPrice(BigDecimal.valueOf(50))
            .build();
    private List<RecipePurchaseOrder> recipePurchaseList1 = new ArrayList<>(Arrays.asList(recipePurchaseOrder1));


    @BeforeEach
    void setup() throws Exception {
        profile.setId(1L);
        profile.setName("ADMIN");

        this.accessToken = this.userLogin();
    }

    private String userLogin() throws Exception {
        Mockito.when(userRepository.findByEmail(any())).thenReturn(Optional.ofNullable(userMock));
        MvcResult result = mockMvc.perform(post("/auth")
                .contentType("application/json").content(userLogin))
                .andExpect(status().isOk())
                .andReturn();

        TypeReference<TokenDto> typeReference = new TypeReference<TokenDto>() {};
        TokenDto token = objectMapper.readValue(result.getResponse().getContentAsString(), typeReference);

        return "Bearer " + token.getToken();
    }

    @Test
    void testPostPurchaseByRecipe() throws Exception {
        Integer recipeId = recipe1.getId();

        Mockito.when(userRepository.findById(any())).thenReturn(Optional.ofNullable(userMock));
        Mockito.when(recipeService.findById(recipeId)).thenReturn(recipe1);
        Mockito.when(batchService.findBatchesByProductIdAndCurrentQuantityGreaterThanEqual(any(), any())).thenReturn(batchList1);
        Mockito.when(purchaseOrderService.save(any())).thenReturn(purchaseOrder1);
        Mockito.when(recipePurchaseOrderRepository.save(any())).thenReturn(recipePurchaseOrder1);

        mockMvc.perform(post("/fresh-products/recipe/purchase")
                .queryParam("recipeId", String.valueOf(recipeId))
                .header("Authorization", accessToken))
                .andExpect(status().isCreated())
                .andReturn();
    }

    @Test
    void testPostPurchaseByRecipeButThereIsNoAvailableBatch() throws Exception {
        Integer recipeId = recipe1.getId();
        List<Batch> emptyBatchList = new ArrayList<>();

        Mockito.when(userRepository.findById(any())).thenReturn(Optional.ofNullable(userMock));
        Mockito.when(recipeService.findById(recipeId)).thenReturn(recipe1);
        Mockito.when(batchService.findBatchesByProductIdAndCurrentQuantityGreaterThanEqual(any(), any())).thenReturn(emptyBatchList);

        mockMvc.perform(post("/fresh-products/recipe/purchase")
                .queryParam("recipeId", String.valueOf(recipeId))
                .header("Authorization", accessToken))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void testGetRecipePurchases() throws Exception {
        Mockito.when(userRepository.findById(any())).thenReturn(Optional.ofNullable(userMock));
        Mockito.when(recipePurchaseOrderRepository.findRecipePurchaseOrderByPurchaseOrder_OrderStatus(OrderStatus.OPENED))
                .thenReturn(recipePurchaseList1);

        mockMvc.perform(get("/fresh-products/recipe/purchase")
                .header("Authorization", accessToken))
                .andExpect(status().isOk())
                .andReturn();
    }
}
