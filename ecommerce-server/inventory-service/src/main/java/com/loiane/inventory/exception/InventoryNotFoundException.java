package com.loiane.inventory.exception;

public class InventoryNotFoundException extends RuntimeException {

    public InventoryNotFoundException(long productId) {
        super("Inventory not found. Product id: " + productId);
    }
}
