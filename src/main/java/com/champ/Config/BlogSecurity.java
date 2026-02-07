package com.champ.Config;

import com.champ.Repo.BlogRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.stereotype.Component;

@Component
public class BlogSecurity {
    @Autowired private BlogRepo blogRepo;
    public boolean isOwner(Long id, String userId){
        return blogRepo.existsByIdAndUserId(id,Long.valueOf(userId));
    }
}
