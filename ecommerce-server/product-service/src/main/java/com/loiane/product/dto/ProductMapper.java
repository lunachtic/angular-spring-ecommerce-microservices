package com.loiane.product.dto;

import com.loiane.product.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public Product toEntity(ProductDTO productDTO) {
        Product product = new Product();
        product.setId(productDTO.id());
        product.setName(productDTO.name());
        product.setDescription(productDTO.description());
        product.setCategory(productDTO.category());
        product.setPrice(productDTO.price());
        return product;
    }

    public ProductDTO toDTO(Product product) {
        return new ProductDTO(product.getId(), product.getName(), product.getDescription(),
                product.getCategory(), product.getPrice());
    }
}
