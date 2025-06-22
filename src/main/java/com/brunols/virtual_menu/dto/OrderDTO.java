package com.brunols.virtual_menu.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record OrderDTO(
        @NotNull(message = "Number is required")
        Integer number,

        @NotBlank(message = "Client name is required")
        String name,

        @NotBlank(message = "Client CPF is required")
        String cpf
) {
}
