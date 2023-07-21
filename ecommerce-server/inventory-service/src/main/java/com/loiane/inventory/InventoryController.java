package com.loiane.inventory;

import com.loiane.inventory.dto.InventoryDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("/{productId}")
    public InventoryDTO getInventoryByProductId(@PathVariable @Positive long productId) {
        return inventoryService.getInventoryByProductId(productId);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public InventoryDTO create(@RequestBody @Valid InventoryDTO inventoryDTO) {
        return inventoryService.createInventory(inventoryDTO);
    }

    @PutMapping("/{productId}")
    public InventoryDTO update(@PathVariable @Positive long productId, @RequestBody @Positive int quantity) {
        return inventoryService.updateInventory(productId, quantity);
    }
}
