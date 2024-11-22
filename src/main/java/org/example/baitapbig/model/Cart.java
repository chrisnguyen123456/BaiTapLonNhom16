package org.example.baitapbig.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private Account user;

    @ManyToOne
    private Book book;

    private Integer quantity;

    @Transient
    private Double totalPrice;

    @Transient
    private Double totalOrderPrice;

}
