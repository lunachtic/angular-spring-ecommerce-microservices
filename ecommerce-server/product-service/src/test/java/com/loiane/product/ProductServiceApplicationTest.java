package com.loiane.product;

import com.loiane.product.config.TestContainersConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(TestContainersConfig.class)
class ProductServiceApplicationTest {

    @Test
    void contextLoads(){
    }
}
