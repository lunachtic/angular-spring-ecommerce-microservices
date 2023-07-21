package com.loiane.inventory;

import com.loiane.inventory.dto.InventoryDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import java.util.stream.Stream;

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
        InventoryDTO inventoryDTO = testRestTemplate.getForObject(BASE_URL + "/" + productId, InventoryDTO.class);

        // then
        assertNotNull(inventoryDTO);
        assertEquals(productId, inventoryDTO.productId());
        assertEquals(inventory.getQuantity(), inventoryDTO.quantity());
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

    /**
     * Method under test: {@link InventoryController#create(InventoryDTO)}
     */
    @Test
    @DisplayName("Should create a new inventory")
    void create() {
        // given
        InventoryDTO inventoryDTO = new InventoryDTO(1L, 10);

        // when
        var responseEntity = testRestTemplate.postForEntity(BASE_URL, inventoryDTO, InventoryDTO.class);

        // then
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(inventoryDTO.productId(), responseEntity.getBody().productId());
        assertEquals(inventoryDTO.quantity(), responseEntity.getBody().quantity());
    }

    /**
     * Method under test: {@link InventoryController#create(InventoryDTO)}
     */
    @ParameterizedTest(name = "Should return error 400 when creating a new inventory with invalid InventoryDTO: {0}")
    @DisplayName("Should return error 400 when creating a new inventory with invalid InventoryDTO")
    @MethodSource("invalidInventoryDTOs")
    void createInvalidInventory(InventoryDTO inventoryDTO) {
        // when
        var responseEntity = testRestTemplate.postForEntity(BASE_URL, inventoryDTO, String.class);

        // then
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    static Stream<Arguments> invalidInventoryDTOs() {
        return Stream.of(
                Arguments.of(new InventoryDTO(1, -1)),
                Arguments.of(new InventoryDTO(0, 0)),
                Arguments.of(new InventoryDTO(0, 1)),
                Arguments.of(new InventoryDTO(-1, 0)),
                Arguments.of(new InventoryDTO(-1, -1))
        );
    }

    /**
     * Method under test: {@link InventoryController#update(long, int)}
     */
    @Test
    @DisplayName("Should update the inventory for a given product id")
    void update() {
        // given
        long productId = 1L;
        Inventory inventory = new Inventory(productId, 10);
        inventoryRepository.save(inventory);

        // when
        var responseEntity = testRestTemplate.exchange(BASE_URL + "/" + productId, HttpMethod.PUT, new HttpEntity<>(20), InventoryDTO.class);

        // then
        Inventory updatedInvetory = inventoryRepository.findByProductId(productId).orElseThrow();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(productId, responseEntity.getBody().productId());
        assertEquals(updatedInvetory.getQuantity(), responseEntity.getBody().quantity());
    }

    /**
     * Method under test: {@link InventoryController#update(long, int)}
     */
    @ParameterizedTest(name = "Should return error 400 when updating the inventory with invalid quantity: {0}")
    @DisplayName("Should return error 400 when updating the inventory with invalid parameters")
    @MethodSource("invalidInventoryDTOs")
    void updateInvalidInventory(InventoryDTO inventoryDTO) {
        // when
        var responseEntity = testRestTemplate.exchange(BASE_URL + "/" + inventoryDTO.productId(), HttpMethod.PUT, new HttpEntity<>(inventoryDTO.quantity()), String.class);

        // then
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }
}
