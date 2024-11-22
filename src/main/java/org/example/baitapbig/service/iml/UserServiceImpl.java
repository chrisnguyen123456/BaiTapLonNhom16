package org.example.baitapbig.service.iml;

import org.example.baitapbig.model.Account;
import org.example.baitapbig.repository.UserRepository;
import org.example.baitapbig.service.UserService;
import org.example.baitapbig.until.AppConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public List<Account> findByRole(String role) {
        return userRepository.findByRole(role);
    }
    @Override
    public Account saveUser(Account account) {
        account.setRole("ROLE_USER");
        account.setIsEnable(true);
        account.setAccountNonLocked(true);
        account.setFailedAttempt(0);

        String encodePassword = passwordEncoder.encode(account.getPassword());
        account.setPassword(encodePassword);
        Account saveUser = userRepository.save(account);
        return saveUser;
    }
    @Override
    public Account getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    @Override
    public Account updateUser(Account user) {
        return userRepository.save(user);
    }
    @Override
    public Boolean updateAccountStatus(Integer id, Boolean status) {

        Optional<Account> findByuser = userRepository.findById(id);

        if (findByuser.isPresent()) {
            Account userDtls = findByuser.get();
            userDtls.setIsEnable(status);
            userRepository.save(userDtls);
            return true;
        }

        return false;
    }

    @Override
    public void increaseFailedAttempt(Account user) {
        int attempt = user.getFailedAttempt() + 1;
        user.setFailedAttempt(attempt);
        userRepository.save(user);
    }

    @Override
    public void userAccountLock(Account user) {
        user.setAccountNonLocked(false);
        user.setLockTime(new Date());
        userRepository.save(user);
    }

    @Override
    public boolean unlockAccountTimeExpired(Account user) {

        long lockTime = user.getLockTime().getTime();
        long unLockTime = lockTime + AppConstant.UNLOCK_DURATION_TIME;

        long currentTime = System.currentTimeMillis();

        if (unLockTime < currentTime) {
            user.setAccountNonLocked(true);
            user.setFailedAttempt(0);
            user.setLockTime(null);
            userRepository.save(user);
            return true;
        }

        return false;
    }
    @Override
    public void updateUserResetToken(String email, String resetToken) {
        Account findByEmail = userRepository.findByEmail(email);
        findByEmail.setResetToken(resetToken);
        userRepository.save(findByEmail);
    }

    @Override
    public Account getUserByToken(String token) {
        return userRepository.findByResetToken(token);
    }


}


