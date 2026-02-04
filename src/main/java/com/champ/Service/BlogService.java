package com.champ.Service;

import com.champ.Entity.Blog;
import com.champ.Entity.User;
import com.champ.Repo.BlogRepo;
import com.champ.Repo.LoginRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BlogService implements IServiceBlog {
    @Autowired
    private LoginRepo lRepo;
    @Autowired
    private BlogRepo bRepo;


    @Override
    public Blog createBlog(String title, String body, String imageUrl, Long id) {
        User user = lRepo.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        Blog newBlog = new Blog(title, body, imageUrl, user);
        return bRepo.save(newBlog);
    }

    @Override
    public List<Blog> fetchBlogs(Long id) {
        return bRepo.findByUserId(id);
    }

    @Override
    public void removeBlog(Long id) {
        bRepo.deleteById(id);
    }

    @Override
    public Blog editBlog(Blog blogRequest, Long id) {
        Blog blog = bRepo.findById(id).orElseThrow(() -> new RuntimeException("Blog not found"));
        if (!blogRequest.getTitle().isBlank()) blog.setTitle(blogRequest.getTitle());
        if (!blogRequest.getBody().isBlank()) blog.setBody(blogRequest.getBody());
        if (blogRequest.getImageUrl()!=null) {blog.setImageUrl(blogRequest.getImageUrl());
            System.out.println("New one"+blogRequest.getImageUrl());
            System.out.println("Old one"+blog.getImageUrl());}
        return bRepo.save(blog);
    }

    @Override
    public Blog fetchById(Long id) {
        Optional<Blog> byId = bRepo.findById(id);
        if(byId.isPresent()) return byId.get();
        else throw new RuntimeException("No blog found");


    }
}
