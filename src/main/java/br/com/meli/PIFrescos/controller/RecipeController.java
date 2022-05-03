package br.com.meli.PIFrescos.controller;

import br.com.meli.PIFrescos.controller.forms.RecipeForm;
import br.com.meli.PIFrescos.models.Recipe;
import br.com.meli.PIFrescos.service.interfaces.IRecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/fresh-products/recipe")
public class RecipeController {

    @Autowired
    private IRecipeService recipeService;

    @GetMapping("/all")
    public ResponseEntity<List<Recipe>> getAll() {
        List<Recipe> recipes = recipeService.getAll();
        return ResponseEntity.ok(recipes);
    }

    @PostMapping("")
    public ResponseEntity<Recipe> create(@RequestBody RecipeForm recipeForm) {
        Recipe recipe = RecipeForm.convert(recipeForm);

        recipeService.save(recipe);

        return new ResponseEntity<>(recipe, HttpStatus.CREATED);
    }
}
