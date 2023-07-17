package com.loiane.product;

import com.loiane.product.dto.ProductDTO;
import com.loiane.product.dto.ProductMapper;
import com.loiane.product.dto.ProductPageDTO;
import com.loiane.product.exception.ProductNotFoundException;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
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

    public ProductDTO findById(long id) {
        return productRepository.findById(id).map(productMapper::toDTO).orElseThrow(() -> new ProductNotFoundException(id));
    }

    public ProductDTO create(@NotNull ProductDTO productDTO) {
        Product product = productMapper.toEntity(productDTO);
        Product savedProduct = productRepository.save(product);
        return productMapper.toDTO(savedProduct);
    }

    public ProductDTO update(long id, ProductDTO productDTO) {
        return productRepository.findById(id)
                .map(product -> {
                    product.setName(productDTO.name());
                    product.setDescription(productDTO.description());
                    product.setCategory(productDTO.category());
                    product.setPrice(productDTO.price());
                    product.setUpdatedAt(LocalDateTime.now());
                    Product updatedProduct = productRepository.save(product);
                    return productMapper.toDTO(updatedProduct);
                })
                .orElseThrow(() -> new ProductNotFoundException(id));
    }
}
