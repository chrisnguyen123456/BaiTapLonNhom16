package org.example.baitapbig.service;

import org.example.baitapbig.model.BookOrder;
import org.example.baitapbig.model.OrderRequest;

import java.util.List;

public interface OrderService {

    public boolean saveOrder(Integer userid, OrderRequest orderRequest);

    List<BookOrder> getAllOrder(Integer userId);
}
