package com.champ.Controller;

import com.champ.Config.JwtService;
import com.champ.Dto.*;
import com.champ.Entity.User;
import com.champ.Repo.LoginRepo;
import com.champ.Service.EmailService;
import com.champ.Service.OtpService;
import com.champ.Service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth/")
public class AuthController {

    @Autowired
    private LoginRepo repo;
    @Autowired private OtpService otpService;
    @Autowired private RedisService redisService;
    @Autowired private EmailService emailService;
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
        @PostMapping("/signup/send-mail")
        public ResponseEntity<HttpStatus> sendMail(@RequestBody EmailRequestDto emailRequestDto) throws Exception {
            User findUser = repo.findByEmail(emailRequestDto.getEmail());
            if(findUser!=null) return new ResponseEntity<>(HttpStatus.CONFLICT);

            String otp = otpService.generateOtp();
            redisService.saveOtp(emailRequestDto.getEmail(), otp);
            emailService.sendVerifyEmail(emailRequestDto.getEmail(),otp);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        
        @PostMapping("/signup/verify")
        public ResponseEntity<HttpStatus> addUser(@RequestBody SignupVerifyDto request) {
            String otp = redisService.getOtp(request.getEmail());
            String receivedOtp = String.valueOf(request.getOtp());
            if (receivedOtp.equals(otp)) {
                String encodedPass = encoder.encode(request.getPass());
                User user = new User(request.getName(),request.getEmail(),encodedPass);
                repo.save(user);
                return new ResponseEntity<>(HttpStatus.CREATED);
            }
            return  new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

    @PostMapping("/signup/check-email")
    public ResponseEntity<HttpStatus> checkEmail(@RequestBody EmailRequestDto  request) throws Exception {
        User user = repo.findByEmail(request.getEmail());
        if(user!=null){
            String otp= otpService.generateOtp();
            redisService.saveOtp(request.getEmail(), otp);
            emailService.sendForgotEmail(request.getEmail(),otp);
            return new ResponseEntity<>(HttpStatus.OK);

        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    String email=null;
    @PostMapping("/login/verify-otp")
    public ResponseEntity<HttpStatus> forgotPass(@RequestBody ForgotPassDto request){
        email=request.getEmail();
        String otp = redisService.getOtp(request.getEmail());
        String receivedOtp = request.getOtp();
        if(receivedOtp.equals(otp)){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("/login/reset-pass")
    public  ResponseEntity<HttpStatus> resetPass(@RequestBody PassRequestDto requestDto){
        User user = repo.findByEmail(email);
        String encodedPass = encoder.encode(requestDto.getNewPass());
        user.setPassHash(encodedPass);
        repo.save(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }



    }

