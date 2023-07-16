package com.loiane.product.api;

import com.loiane.product.BaseIntegrationTest;
import com.loiane.product.Product;
import com.loiane.product.dto.ProductDTO;
import com.loiane.product.dto.ProductPageDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductControllerTest extends BaseIntegrationTest {

    @Test
    void findAll() {
        // given
        Product product = new Product("Product Name", "description", "technology", 10.0);
        productRepository.save(product);

        // when
        ProductPageDTO productPageDTO = testRestTemplate.getForObject("/api/products", ProductPageDTO.class);

        // then
        assertNotNull(productPageDTO);
        assertEquals(1, productPageDTO.totalElements());
        assertEquals(1, productPageDTO.totalPages());
        assertEquals(1, productPageDTO.products().size());
        assertTrue(productPageDTO.products().get(0).id() > 0);
        assertEquals(product.getName(), productPageDTO.products().get(0).name());
    }
}
