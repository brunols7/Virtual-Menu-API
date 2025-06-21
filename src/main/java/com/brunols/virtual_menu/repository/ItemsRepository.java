package com.brunols.virtual_menu.repository;

import com.brunols.virtual_menu.entity.Items;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemsRepository extends JpaRepository<Items, Long> {
    boolean existsByNameIgnoreCase(String name);
}
