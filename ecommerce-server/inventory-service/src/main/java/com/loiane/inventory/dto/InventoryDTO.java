package com.loiane.inventory.dto;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record InventoryDTO(@Positive long productId, @PositiveOrZero int quantity) {
}
