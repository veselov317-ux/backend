package com.backend.sys.repository;

import com.backend.sys.entity.Category;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByActiveTrueOrderByNameAsc();

    boolean existsByNameIgnoreCase(String name);
}
