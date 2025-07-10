package com.brunols.virtual_menu.dto;

import jakarta.validation.constraints.Min;

public record RegisterUserDto(String email,

                              String password,

                              String fullName) {
}
