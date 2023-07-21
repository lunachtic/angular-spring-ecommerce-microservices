package com.loiane.inventory;

import com.loiane.inventory.dto.InventoryDTO;
import com.loiane.inventory.dto.InventoryMapper;
import com.loiane.inventory.exception.InventoryNotFoundException;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
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

    public InventoryDTO createInventory(InventoryDTO inventoryDTO) {
        Inventory inventory = inventoryMapper.toEntity(inventoryDTO);
        return inventoryMapper.toDTO(inventoryRepository.save(inventory));
    }

    public InventoryDTO updateInventory(@Positive long productId, @PositiveOrZero int quantity) {
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new InventoryNotFoundException(productId));
        inventory.setQuantity(quantity);
        return inventoryMapper.toDTO(inventoryRepository.save(inventory));
    }

}
