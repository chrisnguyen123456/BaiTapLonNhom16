package org.example.baitapbig.repository;

import org.example.baitapbig.model.BookOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookOrderRepository extends JpaRepository<BookOrder, InternalError> {
    List<BookOrder> findByUserId(Integer userId);
}
