package com.brunols.virtual_menu.entity;

import com.brunols.virtual_menu.dto.CategoryDTO;
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
@Table(name="categories")
public class Categories {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    public Categories(@Valid CategoryDTO dto) {
        this.name = dto.name();
    }
}
