package com.champ.Controller;

import com.champ.Config.JwtService;
import com.champ.Dto.LoginRequestDto;
import com.champ.Dto.LoginResponseDto;
import com.champ.Entity.User;
import com.champ.Repo.LoginRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = {"https://blog-editorial.netlify.app/"})
@RequestMapping("api/v1/auth/")
public class AuthController {

    @Autowired
    private LoginRepo repo;
    @Autowired
    private AuthenticationManager authManager;
    private String jwt=null;
    @Autowired
    private JwtService jwtService;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto){
        Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(),loginRequestDto.getPass()));
        LoginResponseDto responseDto=null;
        if(authentication.isAuthenticated()){
            User user = repo.findByEmail(loginRequestDto.getEmail());
            jwt = jwtService.generateToken(user.getId().toString());
            responseDto = new LoginResponseDto(jwt);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }
        else {
            responseDto = new LoginResponseDto(jwt);
            return new ResponseEntity<>(responseDto,HttpStatus.UNAUTHORIZED);
        }
    }
        @PostMapping("/signup")
        public ResponseEntity<HttpStatus> signup(@RequestBody LoginRequestDto loginRequestDto){
            String encodedPass = encoder.encode((loginRequestDto.getPass()));
            User user = new User(loginRequestDto.getName(),loginRequestDto.getEmail(),encodedPass);
            repo.save(user);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }


    }

