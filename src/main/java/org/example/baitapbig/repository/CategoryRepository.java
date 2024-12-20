package org.example.baitapbig.repository;

import org.example.baitapbig.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    public Boolean existsByName(String name);

}
