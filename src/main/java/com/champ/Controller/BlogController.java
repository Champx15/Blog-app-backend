package com.champ.Controller;

import com.champ.Config.JwtService;
import com.champ.Entity.Blog;
import com.champ.Service.BlogService;
import com.sun.net.httpserver.HttpsServer;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/v1/blogs")
public class BlogController {
    @Autowired
    private BlogService blogService;
    @Autowired
    private JwtService jwtService;


    @GetMapping
    public ResponseEntity<List<Blog>> fetchBlogs(HttpServletRequest request){
        Long id = jwtService.extractIdFromRequest(request);
        List<Blog> blogs = blogService.fetchBlogs(id);
        return new ResponseEntity<>(blogs,HttpStatus.OK );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Blog> fetchById(@PathVariable Long id){
        Blog blog = blogService.fetchById(id);
        return new ResponseEntity<>(blog,HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<Blog> addBlog(@RequestBody Blog blogRequest,HttpServletRequest request) throws IOException {
        Long id = jwtService.extractIdFromRequest(request);
        Blog newBlog = blogService.createBlog(blogRequest.getTitle(),blogRequest.getBody(),blogRequest.getImageUrl(),id);
        return new ResponseEntity<>(newBlog,HttpStatus.CREATED);
    }
    @PreAuthorize("@blogSecurity.isOwner(#id,authentication.name)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBlog(@PathVariable Long id){
        blogService.removeBlog(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("@blogSecurity.isOwner(#id,authentication.name)")
    @PatchMapping("/{id}")
    public ResponseEntity<Blog> editBlog(@RequestBody Blog blogRequest,@PathVariable Long id){
        Blog blog = blogService.editBlog(blogRequest, id);
        return new ResponseEntity<>(blog, HttpStatus.OK);
    }


}
