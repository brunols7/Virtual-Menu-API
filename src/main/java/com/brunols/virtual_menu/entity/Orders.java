package com.brunols.virtual_menu.entity;

import com.brunols.virtual_menu.dto.OrderDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="orders")
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int commandNumber;

    @Column(nullable = false)
    private String clientName;

    @Column(nullable = false)
    private String clientCpf;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime createdAt;

    public Orders(OrderDTO dto) {
        this.commandNumber = dto.number();
        this.clientName = dto.name();
        this.clientCpf = dto.cpf();
        this.status = Status.OPEN;
        this.createdAt = LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
