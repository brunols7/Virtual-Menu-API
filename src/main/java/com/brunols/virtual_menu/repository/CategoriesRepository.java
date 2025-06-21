package com.brunols.virtual_menu.repository;

import com.brunols.virtual_menu.entity.Categories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriesRepository extends JpaRepository<Categories, Long> {
    boolean existsByNameIgnoreCase(String name);
}
