package org.example.baitapbig.service.iml;

import org.example.baitapbig.model.BookOrder;
import org.example.baitapbig.model.Cart;
import org.example.baitapbig.model.OrderAddress;
import org.example.baitapbig.model.OrderRequest;
import org.example.baitapbig.repository.BookOrderRepository;
import org.example.baitapbig.repository.CartRepository;
import org.example.baitapbig.service.OrderService;
import org.example.baitapbig.until.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private BookOrderRepository bookOrderRepository;

    @Autowired
    private CartRepository cartRepository;

    @Override
    public boolean saveOrder(Integer userid, OrderRequest orderRequest) {
        List<Cart> carts = cartRepository.findByUserId(userid);

        for (Cart cart : carts) {

            BookOrder order = new BookOrder();

            order.setOrderId(UUID.randomUUID().toString());
            order.setOrderDate(new Date());

            order.setBook(cart.getBook());
            order.setPrice(cart.getBook().getPrice());

            order.setQuantity(cart.getQuantity());
            order.setUser(cart.getUser());

            order.setStatus(OrderStatus.IN_PROGRESS.getName());
            order.setPaymentType(orderRequest.getPaymentType());

            OrderAddress address = new OrderAddress();
            address.setFirstName(orderRequest.getFirstName());
            address.setLastName(orderRequest.getLastName());
            address.setEmail(orderRequest.getEmail());
            address.setMobileNo(orderRequest.getMobileNo());
            address.setAddress(orderRequest.getAddress());
            address.setCity(orderRequest.getCity());
            address.setState(orderRequest.getState());
            address.setPincode(orderRequest.getPincode());

            order.setOrderAddress(address);

            bookOrderRepository.save(order);


        }
        return true;
    }

    @Override
    public List<BookOrder> getAllOrder(Integer userId) {
        return (List<BookOrder>) bookOrderRepository.findByUserId(userId);
    }
}
