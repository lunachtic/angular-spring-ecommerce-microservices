package com.loiane.product;

import com.loiane.product.config.TestContainersConfig;
import org.springframework.boot.SpringApplication;

class TestProductApplication {

	public static void main(String[] args) {
		SpringApplication.from(ProductServiceApplication::main).with(TestContainersConfig.class).run(args);
	}
}
