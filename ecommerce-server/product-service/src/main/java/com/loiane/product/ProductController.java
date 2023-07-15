package com.loiane.product;

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

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ProductPageDTO findAll(@RequestParam(defaultValue = "0") @PositiveOrZero int page,
                                  @RequestParam(defaultValue = "10") @Positive @Max(1000) int pageSize) {
        return productService.findAll(page, pageSize);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public ProductDTO create(@RequestBody @Valid ProductDTO productDTO) {
        return productService.create(productDTO);
    }
}
