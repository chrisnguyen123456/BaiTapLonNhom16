package org.example.baitapbig.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique = true)
    private String email;
    private String password;
    private String name;
    private String role;
    private String pincode;
    private String profileImage;
    private Boolean isEnable = false;
    private Boolean accountNonLocked;
    private Integer failedAttempt;
    private Date lockTime;
    private String resetToken;
}
