package com.brunols.virtual_menu.dto;

import jakarta.validation.constraints.NotNull;

public record CategoryDTO(@NotNull(message = "Name is required")String name) {
}
