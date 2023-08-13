package com.loiane.product;

import com.loiane.product.contract.Inventory;
import com.loiane.product.contract.InventoryClient;
import com.loiane.product.dto.ProductDTO;
import com.loiane.product.dto.ProductPageDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("api/products")
public class ProductController {

    private final ProductService productService;

    private final InventoryClient inventoryClient;

    public ProductController(ProductService productService, InventoryClient inventoryClient) {
        this.productService = productService;
        this.inventoryClient = inventoryClient;
    }

    @GetMapping
    public ProductPageDTO findAll(@RequestParam(defaultValue = "0") @PositiveOrZero int page,
                                  @RequestParam(defaultValue = "10") @Positive @Max(100) int pageSize) {
        return productService.findAll(page, pageSize);
    }

    @GetMapping("/{id}")
    public ProductDTO findById(@PathVariable @Positive long id) {
        Inventory inventory = inventoryClient.getInventoryByProductId(id);
        return productService.findById(id);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public ProductDTO create(@RequestBody @Valid ProductDTO productDTO) {
        return productService.create(productDTO);
    }

    @PutMapping("/{id}")
    public ProductDTO update(@PathVariable @Positive long id, @RequestBody @Valid ProductDTO productDTO) {
        return productService.update(id, productDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @Positive long id) {
        productService.delete(id);
    }
}
