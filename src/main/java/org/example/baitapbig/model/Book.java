package org.example.baitapbig.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(length = 500)
    private String title;
    @Column(length = 500)
    private String author;
    private double price;
    private String image;
    private String category;
}
