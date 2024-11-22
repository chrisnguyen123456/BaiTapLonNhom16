package org.example.baitapbig.until;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.example.baitapbig.model.Account;
import org.example.baitapbig.model.BookOrder;
import org.example.baitapbig.model.Cart;
import org.example.baitapbig.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.List;

@Component
public class CommonUtil {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserRepository userRepository;

    public Boolean sendMail(String url, String reciepentEmail) throws UnsupportedEncodingException, MessagingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("vinhubuntu@gmail.com", "Shopping Cart");
        helper.setTo(reciepentEmail);

        String content = "<p>Hello,</p>" + "<p>You have requested to reset your password.</p>"
                + "<p>Click the link below to change your password:</p>" + "<p><a href=\"" + url
                + "\">Change my password</a></p>";
        helper.setSubject("Password Reset");
        helper.setText(content, true);
        mailSender.send(message);
        return true;
    }

    public Boolean sendMailR(String url, String reciepentEmail) throws UnsupportedEncodingException, MessagingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("vinhubuntu@gmail.com", "Shopping Cart");
        helper.setTo(reciepentEmail);

        //verify email
        String content = "<p>Hello,</p>" + "<p>You have requested to verify your email.</p>"
                + "<p>Click the link below to verify your email:</p>" + "<p><a href=\"" + url
                + "\">Verify my email</a></p>";
        helper.setSubject("Verify Email");
        helper.setText(content, true);
        mailSender.send(message);
        return true;
    }

    private String generateCartContent(List<Cart> cartItems, Double totalOrderPrice, List<BookOrder> order) {
        StringBuilder content = new StringBuilder();

        // Add order details
        content.append("<h2>Order Details</h2>")
                .append("<p>Order ID: ").append(order.get(0).getOrderId()).append("</p>")
                .append("<p>Order Date: ").append(order.get(0).getOrderDate()).append("</p>")
                .append("<p>Payment Type: ").append(order.get(0).getPaymentType()).append("</p>")
                .append("<p>Order Status: ").append(order.get(0).getStatus()).append("</p>");

        // Add table with cart items
        content.append("<h2>Cart Items</h2>")
                .append("<table border='1'>")
                .append("<tr>")
                .append("<th>Product Name</th>")
                .append("<th>Quantity</th>")
                .append("<th>Price</th>")
                .append("<th>Total</th>")
                .append("</tr>");

        for (Cart item : cartItems) {
            double itemTotal = item.getQuantity() * item.getBook().getPrice();
            content.append("<tr>")
                    .append("<td>").append(item.getBook().getTitle()).append("</td>")
                    .append("<td>").append(item.getQuantity()).append("</td>")
                    .append("<td>").append(item.getBook().getPrice()).append("</td>")
                    .append("<td>").append(itemTotal).append("</td>")
                    .append("</tr>");
        }

        content.append("<tr>")
                .append("<td colspan='3'>Total Order Price</td>")
                .append("<td>").append(totalOrderPrice).append("</td>")
                .append("</tr>")
                .append("</table>");

        return content.toString();
    }



    public Boolean sendMailOrder(String url, String reciepentEmail, List<Cart> carts, double totalPrice ,List<BookOrder> order) throws UnsupportedEncodingException, MessagingException {

        Account user = userRepository.findByEmail(reciepentEmail);

        // print table list cart content
        String content = generateCartContent(carts, totalPrice, order);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom("vinhubuntu@gmail.com", "Shopping Cart");
        helper.setTo(reciepentEmail);
        helper.setSubject("Bill for " + user.getId() + "Order");
        helper.setText(content, true);
        mailSender.send(message);
        return true;
    }

    public static String generateUrl(HttpServletRequest request) {

        // http://localhost:8080/forgot-password
        String siteUrl = request.getRequestURL().toString();

        return siteUrl.replace(request.getServletPath(), "");

    }
}
