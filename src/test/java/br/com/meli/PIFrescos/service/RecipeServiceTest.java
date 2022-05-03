package br.com.meli.PIFrescos.service;

import br.com.meli.PIFrescos.models.Recipe;
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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@ExtendWith(MockitoExtension.class)
class RecipeServiceTest {

    @Mock
    RecipeRepository recipeRepository;

    @InjectMocks
    RecipeService recipeService;

    private Recipe recipe1 = new Recipe(1, "recipe 1", new ArrayList<>());
    private Recipe recipe2 = new Recipe(2, "recipe 2", new ArrayList<>());

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
        Recipe newValues = new Recipe(null, "new name", null);

        Mockito.when(recipeRepository.findById(id)).thenReturn(Optional.of(recipe1));
        Mockito.when(recipeRepository.save(recipe1)).thenReturn(recipe1);

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