package com.brunols.virtual_menu.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ItemDTO(

        @NotNull(message = "Name is required")
        String name,

        @NotNull(message = "Description is required")
        String description,

        @NotNull(message = "Price is required")
        int price,

        @NotNull(message = "Category ID is required")
        Long categoryId
) {
}
