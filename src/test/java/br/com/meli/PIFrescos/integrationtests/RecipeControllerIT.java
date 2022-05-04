package br.com.meli.PIFrescos.integrationtests;

import br.com.meli.PIFrescos.controller.dtos.RecipeDTO;
import br.com.meli.PIFrescos.controller.dtos.TokenDto;
import br.com.meli.PIFrescos.controller.forms.IngredientForm;
import br.com.meli.PIFrescos.controller.forms.RecipeForm;
import br.com.meli.PIFrescos.models.Product;
import br.com.meli.PIFrescos.models.Profile;
import br.com.meli.PIFrescos.models.Recipe;
import br.com.meli.PIFrescos.models.RecipeIngredient;
import br.com.meli.PIFrescos.models.StorageType;
import br.com.meli.PIFrescos.models.User;
import br.com.meli.PIFrescos.models.UserRole;
import br.com.meli.PIFrescos.repository.RecipeRepository;
import br.com.meli.PIFrescos.repository.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
public class RecipeControllerIT {

    @MockBean
    private RecipeRepository recipeRepository;

    // user repository aqui?
    @MockBean
    private UserRepository userRepository;

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
    void testGetAllRecipes() throws Exception {
        Mockito.when(userRepository.findById(any())).thenReturn(Optional.ofNullable(userMock));
        Mockito.when(recipeRepository.findAll()).thenReturn(recipes);

        MvcResult result = mockMvc.perform(get("/fresh-products/recipe/all")
                .header("Authorization", accessToken))
                .andExpect(status().isOk())
                .andReturn();

        TypeReference<List<RecipeDTO>> typeReference = new TypeReference<List<RecipeDTO>>() {};

        List<RecipeDTO> returnList = objectMapper.readValue(result.getResponse().getContentAsString(), typeReference);

        assertEquals(recipes.size(), returnList.size());
    }

    @Test
    void testCreateNewRecipe() throws Exception {
        Mockito.when(userRepository.findById(any())).thenReturn(Optional.ofNullable(userMock));
        Mockito.when(recipeRepository.save(any())).thenReturn(recipe1);

        String payloadUpdated = objectMapper.writeValueAsString(recipeForm1);

        MvcResult result = mockMvc.perform(post("/fresh-products/recipe")
                .header("Authorization", accessToken)
                .contentType(MediaType.APPLICATION_JSON).content(payloadUpdated))
                .andExpect(status().isCreated())
                .andReturn();

        RecipeDTO returnValue = objectMapper.readValue(result.getResponse().getContentAsString(), RecipeDTO.class);

        assertEquals(recipe1.getName(), returnValue.getName());
        assertEquals(recipe1.getIngredients().size(), returnValue.getIngredients().size());
    }

    @Test
    void testUpdateAnExistingRecipe() throws Exception {
        String newName = "recipe1 new name";
        recipeForm1.setName(newName);
        recipe1.setName(newName);
        Integer recipeId = recipe1.getId();

        Mockito.when(userRepository.findById(any())).thenReturn(Optional.ofNullable(userMock));
        Mockito.when(recipeRepository.findById(any())).thenReturn(Optional.ofNullable(recipe1));
        Mockito.when(recipeRepository.save(any())).thenReturn(recipe1);

        String payloadUpdated = objectMapper.writeValueAsString(recipeForm1);

        MvcResult result = mockMvc.perform(put("/fresh-products/recipe")
                .queryParam("recipeId", String.valueOf(recipeId))
                .header("Authorization", accessToken)
                .contentType(MediaType.APPLICATION_JSON).content(payloadUpdated))
                .andExpect(status().isCreated())
                .andReturn();

        RecipeDTO returnValue = objectMapper.readValue(result.getResponse().getContentAsString(), RecipeDTO.class);

        assertEquals(newName, returnValue.getName());
    }

    @Test
    void testUpdateUnknownRecipeIdFails() throws Exception {
        Integer recipeId = recipe1.getId();
        String errorMsg = "Recipe not found.";

        Mockito.when(userRepository.findById(any())).thenReturn(Optional.ofNullable(userMock));
        Mockito.when(recipeRepository.findById(any())).thenReturn(Optional.empty());

        String payloadUpdated = objectMapper.writeValueAsString(recipeForm1);

        MvcResult result = mockMvc.perform(put("/fresh-products/recipe")
                .queryParam("recipeId", String.valueOf(recipeId))
                .header("Authorization", accessToken)
                .contentType(MediaType.APPLICATION_JSON).content(payloadUpdated))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(errorMsg))
                .andReturn();
    }

    @Test
    void testDeleteRecipe() throws Exception {
        Integer recipeId = recipe1.getId();

        Mockito.when(userRepository.findById(any())).thenReturn(Optional.ofNullable(userMock));

        mockMvc.perform(delete("/fresh-products/recipe")
                .queryParam("recipeId", String.valueOf(recipeId))
                .header("Authorization", accessToken))
                .andExpect(status().isAccepted());

        Mockito.verify(recipeRepository).deleteById(recipeId);
    }

}
