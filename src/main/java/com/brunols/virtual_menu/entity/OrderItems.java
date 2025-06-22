package com.brunols.virtual_menu.entity;

import com.brunols.virtual_menu.dto.OrderItemsDTO;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="order_items")
public class OrderItems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Orders order;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Items item;

    private int quantity;

    private String observations;

    public OrderItems(@Valid OrderItemsDTO dto, Orders order, Items item) {
        this.order = order;
        this.item = item;
        this.observations = dto.observations();
        this.quantity = dto.quantity();
    }
}
