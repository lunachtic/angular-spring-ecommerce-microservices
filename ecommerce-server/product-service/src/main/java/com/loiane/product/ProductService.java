package com.loiane.product;

import com.loiane.product.dto.ProductDTO;
import com.loiane.product.dto.ProductMapper;
import com.loiane.product.dto.ProductPageDTO;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Validated
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    public ProductPageDTO findAll(@PositiveOrZero int page, @Positive @Max(1000) int pageSize) {
        Page<Product> productPage = productRepository.findAll(PageRequest.of(page, pageSize));
        List<ProductDTO> productDTOList = productPage.stream().map(productMapper::toDTO).toList();
        return new ProductPageDTO(productDTOList, productPage.getTotalElements(), productPage.getTotalPages());
    }

    public ProductDTO create(ProductDTO productDTO) {
        Product product = productMapper.toEntity(productDTO);
        Product savedProduct = productRepository.save(product);
        return productMapper.toDTO(savedProduct);
    }
}
