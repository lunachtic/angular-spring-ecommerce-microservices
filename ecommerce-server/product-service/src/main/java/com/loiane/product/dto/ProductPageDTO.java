package com.loiane.product.dto;

import java.util.List;

public record ProductPageDTO(List<ProductDTO> products, long totalElements, int totalPages) {
}
