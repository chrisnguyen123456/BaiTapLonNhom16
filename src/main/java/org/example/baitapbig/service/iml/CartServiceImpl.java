package org.example.baitapbig.service.iml;

import org.example.baitapbig.model.Account;
import org.example.baitapbig.model.Book;
import org.example.baitapbig.model.Cart;
import org.example.baitapbig.repository.BookRepository;
import org.example.baitapbig.repository.CartRepository;
import org.example.baitapbig.repository.UserRepository;
import org.example.baitapbig.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;


    @Override
    public Cart saveCart(Integer productId, Integer userId) {
        Account userDtls = userRepository.findById(userId).get();
        Book product = bookRepository.findById(productId).get();

        Cart cartStatus = cartRepository.findByBookIdAndUserId(productId, userId);

        Cart cart = null;

        if (ObjectUtils.isEmpty(cartStatus)) {
            cart = new Cart();
            cart.setBook(product);
            cart.setUser(userDtls);
            cart.setQuantity(1);
            cart.setTotalPrice(1 * product.getPrice());
        } else {
            cart = cartStatus;
            cart.setQuantity(cart.getQuantity() + 1);
            cart.setTotalPrice(cart.getQuantity() * cart.getBook().getPrice());
        }
        Cart saveCart = cartRepository.save(cart);

        return saveCart;
    }

    @Override
    public List<Cart> getCartsByUser(Integer userId) {
        List<Cart> carts = cartRepository.findByUserId(userId);

        Double totalOrderPrice = 0.0;
        List<Cart> updateCarts = new ArrayList<>();
        for (Cart c : carts) {
            Double totalPrice = (c.getBook().getPrice() * c.getQuantity());
            c.setTotalPrice(totalPrice);
            totalOrderPrice = totalOrderPrice + totalPrice;
            c.setTotalOrderPrice(totalOrderPrice);
            updateCarts.add(c);
        }

        return updateCarts;
    }

    @Override
    public Integer getCountCart(Integer userId) {
        Integer countByUserId = cartRepository.countByUserId(userId);
        return countByUserId;
    }

    @Override
    public void updateQuantity(String sy, Integer cid) {
        Cart cart = cartRepository.findById(cid).get();
        int updateQuantity;

        if (sy.equalsIgnoreCase("de")) {
            updateQuantity = cart.getQuantity() - 1;

            if (updateQuantity <= 0) {
                cartRepository.delete(cart);
            } else {
                cart.setQuantity(updateQuantity);
                cartRepository.save(cart);
            }

        } else {
            updateQuantity = cart.getQuantity() + 1;
            cart.setQuantity(updateQuantity);
            cartRepository.save(cart);
        }
    }

    @Override
    @Transactional
    public void deleteCartByUser(Integer userId) {
        System.out.println("userId: " + userId);
        cartRepository.deleteByUserId(userId);
    }
}
