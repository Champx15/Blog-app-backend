package com.champ.Controller;

import com.champ.Config.JwtService;
import com.champ.Dto.UserResponseDto;
import com.champ.Entity.User;
import com.champ.Repo.LoginRepo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@CrossOrigin(origins = {"https://blog-editorial.netlify.app/"})
@RequestMapping("api/v1/me")
public class UserController {

    @Autowired
    private AuthenticationManager authManager;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private LoginRepo loginRepo;

    @GetMapping
    public ResponseEntity<UserResponseDto> getUser(HttpServletRequest request) {
//       String email= jwtService.extractIdFromRequest(request);
       Long id= jwtService.extractIdFromRequest(request);
        Optional<User> optionalUser = loginRepo.findById(id);
        User user=null;
        if(optionalUser.isPresent()){
            user=optionalUser.get();
        }
        assert user != null;
        return new ResponseEntity<>(new UserResponseDto(user.getId(),user.getEmail(),user.getName()), HttpStatus.OK);
    }
}
