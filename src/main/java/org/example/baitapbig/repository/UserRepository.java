package org.example.baitapbig.repository;

import org.example.baitapbig.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<Account, Integer> {
    public List<Account> findByRole(String role);
    public Account findByEmail(String email);
    public Account findByResetToken(String token);
}
