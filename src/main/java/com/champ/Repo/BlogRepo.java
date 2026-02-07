package com.champ.Repo;

import com.champ.Entity.Blog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BlogRepo extends JpaRepository<Blog,Long> {
    List<Blog> findByUserId(Long id);
   boolean existsByIdAndUserId(Long id, Long userId);
}
