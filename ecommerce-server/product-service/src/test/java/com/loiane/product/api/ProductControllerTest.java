package com.loiane.product.api;

import com.loiane.product.BaseIntegrationTest;
import com.loiane.product.Product;
import com.loiane.product.ProductController;
import com.loiane.product.dto.ProductDTO;
import com.loiane.product.dto.ProductPageDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class ProductControllerTest extends BaseIntegrationTest {

    private final String BASE_URL = "/api/products";

    /**
     * Method under test: {@link ProductController#create(ProductDTO)}
     */
    @Test
    void create() {
        // given
        ProductDTO productDTO = new ProductDTO(null, "Product Name", "description", "technology", 10.0);

        // when
        ProductDTO createdProductDTO = testRestTemplate.postForObject(BASE_URL, productDTO, ProductDTO.class);

        // then
        assertNotNull(createdProductDTO);
        assertTrue(createdProductDTO.id() > 0);
        assertEquals(productDTO.name(), createdProductDTO.name());
        assertEquals(productDTO.description(), createdProductDTO.description());
        assertEquals(productDTO.category(), createdProductDTO.category());
        assertEquals(productDTO.price(), createdProductDTO.price());
    }

    /**
     * Method under test: {@link ProductController#findById(long)}
     */
    @Test
    @DisplayName("Should return a product by id when product exists")
    void findById() {
        // given
        Product product = new Product("Product Name", "description", "technology", 10.0);
        productRepository.save(product);

        // when
        ProductDTO productDTO = testRestTemplate.getForObject(BASE_URL + "/" + product.getId(), ProductDTO.class);

        // then
        assertNotNull(productDTO);
        assertEquals(product.getId(), productDTO.id());
        assertEquals(product.getName(), productDTO.name());
        assertEquals(product.getDescription(), productDTO.description());
        assertEquals(product.getCategory(), productDTO.category());
        assertEquals(product.getPrice(), productDTO.price());
    }

    /**
     * Method under test: {@link ProductController#findById(long)}
     */
    @Test
    @DisplayName("Should return an exception when searching for a product by id that does not exist")
    void findByIdNotFound() {
        // given
        long id = 1L;

        // when
        var responseEntity = testRestTemplate.getForEntity(BASE_URL + "/" + id, String.class);

        // then
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    /**
     * Method under test: {@link ProductController#findAll(int, int)}
     */
    @Test
    @DisplayName("Should return a list of products using pagination")
    void findAll() {
        // given
        Product product = new Product("Product Name", "description", "technology", 10.0);
        productRepository.save(product);

        // when
        ProductPageDTO productPageDTO = testRestTemplate.getForObject(BASE_URL, ProductPageDTO.class);

        // then
        assertNotNull(productPageDTO);
        assertEquals(1, productPageDTO.totalElements());
        assertEquals(1, productPageDTO.totalPages());
        assertEquals(1, productPageDTO.products().size());
        assertTrue(productPageDTO.products().get(0).id() > 0);
        assertEquals(product.getName(), productPageDTO.products().get(0).name());
        assertEquals(product.getDescription(), productPageDTO.products().get(0).description());
        assertEquals(product.getCategory(), productPageDTO.products().get(0).category());
        assertEquals(product.getPrice(), productPageDTO.products().get(0).price());
    }
}
