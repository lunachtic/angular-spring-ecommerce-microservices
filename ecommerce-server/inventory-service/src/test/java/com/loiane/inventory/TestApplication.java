package com.loiane.inventory;

import org.springframework.boot.SpringApplication;

public class TestApplication {

    public static void main(String[] args) {
        SpringApplication.from(InventoryServiceApplication::main).with(TestContainersConfig.class).run(args);
    }
}
