package com.champ.Controller;

import com.champ.Config.JwtService;
import com.champ.Entity.Blog;
import com.champ.Service.BlogService;
import com.sun.net.httpserver.HttpsServer;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/v1/blogs")
@CrossOrigin(origins = {"https://blog-editorial.netlify.app/"})
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


    @PostMapping("/add")   //id of user not note
    public ResponseEntity<Blog> addBlog(@RequestBody Blog blogRequest,HttpServletRequest request) throws IOException {
        Long id = jwtService.extractIdFromRequest(request);
        Blog newBlog = blogService.createBlog(blogRequest.getTitle(),blogRequest.getBody(),blogRequest.getImageUrl(),id);
        return new ResponseEntity<>(newBlog,HttpStatus.CREATED);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> deleteBlog(@PathVariable Long id){
        blogService.removeBlog(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/edit/{id}")
    public ResponseEntity<Blog> editBlog(@RequestBody Blog blogRequest,@PathVariable Long id){
        Blog blog = blogService.editBlog(blogRequest, id);
        return new ResponseEntity<>(blog, HttpStatus.OK);
    }


}
