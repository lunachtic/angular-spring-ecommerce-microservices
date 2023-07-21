package com.loiane.inventory;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestContainersConfig.class)
class InventoryControllerTest {

    private static final String BASE_URL = "/api/inventory";

    @Autowired
    TestRestTemplate testRestTemplate;

    @Autowired
    InventoryRepository inventoryRepository;

    @AfterEach
    void cleanUp() {
        inventoryRepository.deleteAll();
    }

    /**
     * Method under test: {@link InventoryController#getInventoryByProductId(long)}
     */
    @Test
    @DisplayName("Should return the inventory for a given product id")
    void shouldReturnInventoryForGivenProductId() {
        // given
        long productId = 1L;
        Inventory inventory = new Inventory(productId, 10);
        inventoryRepository.save(inventory);

        // when
        Inventory result = testRestTemplate.getForObject(BASE_URL + "/" + productId, Inventory.class);

        // then
        assertNotNull(result);
        assertEquals(productId, result.getProductId());
        assertEquals(inventory.getQuantity(), result.getQuantity());
    }

    /**
     * Method under test: {@link InventoryController#getInventoryByProductId(long)}
     */
    @Test
    @DisplayName("Should return 404 when inventory is not found for a given product id")
    void shouldReturn404WhenInventoryIsNotFoundForGivenProductId() {
        // given
        long productId = 1L;

        // when
        var result = testRestTemplate.getForEntity(BASE_URL + "/" + productId, String.class);

        // then
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertEquals("Inventory not found. Product id: " + productId, result.getBody());
    }

    /**
     * Method under test: {@link InventoryController#getInventoryByProductId(long)}
     */
    @Test
    @DisplayName("Should return 404 status when searching for the inventory with invalid path variable")
    void findByIdInvalidPathVariable() {
        // when
        var responseEntity = testRestTemplate.getForEntity(BASE_URL + "/-1", String.class);

        // then
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }
}
