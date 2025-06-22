package com.brunols.virtual_menu.dto;

import com.brunols.virtual_menu.entity.Items;
import com.brunols.virtual_menu.entity.OrderItems;

public record OrderItemsResponseDTO(
        Long itemId,
        String itemName,
        double itemPrice,
        String observations,
        int quantity
) {

    public static OrderItemsResponseDTO fromEntity(OrderItems entity) {
        return new OrderItemsResponseDTO(
                entity.getItem().getId(),
                entity.getItem().getName(),
                entity.getItem().getPrice(),
                entity.getObservations(),
                entity.getQuantity()
        );
    }

}
