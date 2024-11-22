package org.example.baitapbig.service;

import java.util.List;

import org.example.baitapbig.model.Cart;

public interface CartService {
    public Cart saveCart(Integer productId, Integer userId);

    public List<Cart> getCartsByUser(Integer userId);

    public Integer getCountCart(Integer userId);

    public void updateQuantity(String sy, Integer cid);

    public void deleteCartByUser(Integer userId);
}
