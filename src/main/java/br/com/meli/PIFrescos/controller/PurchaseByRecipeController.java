package br.com.meli.PIFrescos.controller;

import br.com.meli.PIFrescos.config.security.TokenService;
import br.com.meli.PIFrescos.controller.dtos.TotalPriceDTO;
import br.com.meli.PIFrescos.models.PurchaseOrder;
import br.com.meli.PIFrescos.service.interfaces.IPurchaseByRecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/fresh-products/recipe/buy")
public class PurchaseByRecipeController {

    @Autowired
    IPurchaseByRecipeService purchaseByRecipeService;

    @Autowired
    private TokenService tokenService;

    @PostMapping("")
    public ResponseEntity<TotalPriceDTO> purchase(@RequestParam Integer recipeId) {
       PurchaseOrder purchaseOrder = purchaseByRecipeService.purchase(recipeId, tokenService.getUserLogged());

        BigDecimal totalPrice = purchaseByRecipeService.calculateTotalPrice(purchaseOrder);

        return new ResponseEntity<>(new TotalPriceDTO(totalPrice), HttpStatus.CREATED);
    }

}
