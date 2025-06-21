package com.brunols.virtual_menu.repository;

import com.brunols.virtual_menu.entity.Orders;
import com.brunols.virtual_menu.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Long> {
    boolean existsByClientCpf(String clientCpf);

    Optional<Orders> findByClientCpfAndStatus(String clientCpf, Status status);
}
