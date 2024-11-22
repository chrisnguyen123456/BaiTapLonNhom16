package org.example.baitapbig.config;

import org.example.baitapbig.model.Account;
import org.example.baitapbig.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Account user = userRepository.findByEmail(username);
        System.out.println(user);
        if (user == null) {
            throw new UsernameNotFoundException("user not found");
        }
        return new CustomUser(user);
    }
}
