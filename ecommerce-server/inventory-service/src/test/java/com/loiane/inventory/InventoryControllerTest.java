package com.loiane.inventory;

import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestContainersConfig.class)
public class InventoryControllerTest {

    @Autowired
    protected TestRestTemplate testRestTemplate;

    @Autowired
    protected InventoryRepository inventoryRepository;

    @AfterEach
    void cleanUp() {
        inventoryRepository.deleteAll();
    }
}
