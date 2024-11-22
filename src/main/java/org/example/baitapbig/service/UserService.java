package org.example.baitapbig.service;

import org.example.baitapbig.model.Account;

import java.util.List;

public interface UserService {
    public List<Account> findByRole(String role);
    public Account saveUser(Account account);
    public Account getUserByEmail(String email);
    public Account updateUser(Account user);
    public void increaseFailedAttempt(Account user);

    public void userAccountLock(Account user);

    public boolean unlockAccountTimeExpired(Account user);
    public Boolean updateAccountStatus(Integer id, Boolean status);
    public void updateUserResetToken(String email, String resetToken);
    public Account getUserByToken(String token);

}
