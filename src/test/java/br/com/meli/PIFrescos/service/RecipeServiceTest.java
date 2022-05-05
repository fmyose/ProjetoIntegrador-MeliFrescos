package br.com.meli.PIFrescos.service;

import br.com.meli.PIFrescos.models.Product;
import br.com.meli.PIFrescos.models.Recipe;
import br.com.meli.PIFrescos.models.RecipeIngredient;
import br.com.meli.PIFrescos.models.StorageType;
import br.com.meli.PIFrescos.repository.RecipeRepository;
import br.com.meli.PIFrescos.service.interfaces.IRecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;


@ExtendWith(MockitoExtension.class)
class RecipeServiceTest {

    @Mock
    RecipeRepository recipeRepository;

    @Mock
    ProductService productService;

    @InjectMocks
    RecipeService recipeService;

    private Product product1 = new Product(1, "product 1",StorageType.FRESH, "product 1 description");
    private Product product2 = new Product(2, "product 2",StorageType.FRESH, "product 2 description");
    private Product product3 = new Product(3, "product 3",StorageType.FRESH, "product 3 description");
    private RecipeIngredient ingredient1 = new RecipeIngredient(1, product1, 1);
    private RecipeIngredient ingredient2 = new RecipeIngredient(2, product2, 2);
    private RecipeIngredient ingredient3 = new RecipeIngredient(3, product3, 3);
    private List<RecipeIngredient> ingredients = new ArrayList<>(Arrays.asList(ingredient1, ingredient2));
    private Recipe recipe1 = new Recipe(1, "recipe 1", ingredients);
    private Recipe recipe2 = new Recipe(2, "recipe 2", ingredients);

    @BeforeEach
    void setUp() {
    }

    @Test
    void getAll() {
        List<Recipe> recipeList = new ArrayList<>();
        recipeList.add(recipe1);
        recipeList.add(recipe2);

        Mockito.when(recipeRepository.findAll()).thenReturn(recipeList);

        List<Recipe> returnValue = recipeService.getAll();

        assertEquals(recipeList.size(), returnValue.size());
        assertEquals(recipeList.get(0), returnValue.get(0));
        assertEquals(recipeList.get(1), returnValue.get(1));
    }

    @Test
    void save() {
        Mockito.when(recipeRepository.save(recipe1)).thenReturn(recipe1);

        recipeService.save(recipe1);
        Mockito.verify(recipeRepository).save(recipe1);
    }

    @Test
    void update() {
        Integer id = recipe1.getId();
        ingredients.add(ingredient3);
        Recipe newValues = new Recipe(null, "new name", ingredients);

        Mockito.when(recipeRepository.findById(id)).thenReturn(Optional.of(recipe1));
        Mockito.when(recipeRepository.save(recipe1)).thenReturn(recipe1);
        Mockito.when(productService.findProductById(any())).thenReturn(product1);
        Recipe returnValue = recipeService.update(id, newValues);

        assertEquals(id, returnValue.getId());
        assertEquals(newValues.getName(), returnValue.getName());
        assertEquals(newValues.getIngredients(), returnValue.getIngredients());
    }

    @Test
    void updateButIdNotFound() {
        Integer notValidId = 111;
        Recipe newValues = new Recipe(null, "new name", null);
        String errorMessage = "Recipe not found.";

        Mockito.when(recipeRepository.findById(notValidId)).thenReturn(Optional.ofNullable(null));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> recipeService.update(notValidId, newValues));

        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    void remove() {
        recipeService.remove(recipe1);

        Mockito.verify(recipeRepository).delete(recipe1);
    }

    @Test
    void removeById() {
        Integer id = recipe1.getId();

        recipeService.removeById(id);
        Mockito.verify(recipeRepository).deleteById(id);
    }
}