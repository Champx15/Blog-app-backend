package com.champ.Repo;

import com.champ.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginRepo extends JpaRepository<User,Long> {
    User findByEmail(String email);
}
