package com.champ.Config;

import com.champ.Entity.User;
import com.champ.Repo.LoginRepo;
import org.jspecify.annotations.NullMarked;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailService implements UserDetailsService {
    @Autowired
    private LoginRepo repo;

    @Override
    @NullMarked
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = repo.findByEmail(email);
        if (user == null) throw new UsernameNotFoundException("User doesn't exist");
        return new UserPrincipal(user);

    }

    public UserDetails loadUserById(Long id) throws UsernameNotFoundException {
        User user = repo.findById(id).orElseThrow(() -> new RuntimeException("User doesn't exists"));
        return new UserPrincipal(user);
    }
}
