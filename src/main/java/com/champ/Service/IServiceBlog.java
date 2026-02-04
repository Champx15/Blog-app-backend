package com.champ.Service;

import com.champ.Entity.Blog;

import java.util.List;

public interface IServiceBlog {

     Blog createBlog(String title, String body, String imageUrl, Long id);
     List<Blog> fetchBlogs(Long id);
     void removeBlog(Long id);
     Blog editBlog(Blog blogRequest, Long id);
     Blog fetchById(Long id);

}

