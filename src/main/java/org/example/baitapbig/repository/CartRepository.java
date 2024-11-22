package org.example.baitapbig.repository;

import org.example.baitapbig.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Integer> {
    Cart findByBookIdAndUserId(Integer bookId, Integer userId);

    Integer countByUserId(Integer userId);

    List<Cart> findByUserId(Integer userId);

    void deleteByUserId(Integer userId);
}
