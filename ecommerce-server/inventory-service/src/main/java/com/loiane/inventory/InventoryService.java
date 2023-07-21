package com.loiane.inventory;

import com.loiane.inventory.dto.InventoryDTO;
import com.loiane.inventory.dto.InventoryMapper;
import com.loiane.inventory.exception.InventoryNotFoundException;
import jakarta.validation.constraints.Positive;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    private final InventoryMapper inventoryMapper;

    public InventoryService(InventoryRepository inventoryRepository, InventoryMapper inventoryMapper) {
        this.inventoryRepository = inventoryRepository;
        this.inventoryMapper = inventoryMapper;
    }

    public InventoryDTO getInventoryByProductId(@Positive long productId) {
        return inventoryRepository.findByProductId(productId).map(inventoryMapper::toDTO)
                .orElseThrow(() -> new InventoryNotFoundException(productId));
    }


}
