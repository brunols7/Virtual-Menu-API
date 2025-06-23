package com.brunols.virtual_menu.entity;

import com.brunols.virtual_menu.dto.ItemDTO;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="items")
public class Items {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private Double price;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Categories category;

    public Items(@Valid ItemDTO dto, Categories category) {

        this.category = category;

    }
}
