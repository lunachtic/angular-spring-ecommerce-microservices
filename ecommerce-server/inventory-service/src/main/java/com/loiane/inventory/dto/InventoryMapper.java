package com.loiane.inventory.dto;

import com.loiane.inventory.Inventory;
import org.springframework.stereotype.Component;

@Component
public class InventoryMapper {

    public InventoryDTO toDTO(Inventory inventory) {
        return new InventoryDTO(inventory.getProductId(), inventory.getQuantity());
    }

    public Inventory toEntity(InventoryDTO inventoryDTO) {
        return new Inventory(inventoryDTO.productId(), inventoryDTO.quantity());
    }
}
