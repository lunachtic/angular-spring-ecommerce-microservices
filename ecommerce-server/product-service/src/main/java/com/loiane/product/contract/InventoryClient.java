package com.loiane.product.contract;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("inventory-service")
public interface InventoryClient {

    @GetMapping(value = "/api/inventory/{productId}")
    Inventory getInventoryByProductId(@PathVariable("productId") long productId);
}
