package com.champ.Config;


import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private final String secretKey;

    public JwtService() {
        this.secretKey = getTheSecretKey();
    }

    private String getTheSecretKey() {
        Dotenv dotenv = Dotenv.load();
        String base64Key = dotenv.get("HMAC_SECRET");
        if (base64Key == null || base64Key.isEmpty()) {
            throw new RuntimeException("HMAC_SECRET not set in environment variables!");
        }
        return base64Key;
    }

    public String generateToken(String id) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder().claims(claims).subject(id).issuedAt(new Date(System.currentTimeMillis())).expiration(new Date(System.currentTimeMillis() + 12_960_000_000L)).signWith(getKey(), SignatureAlgorithm.HS256).compact();
    }

    private Key getKey() {
        byte[] b = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(b);

    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String extractUserName(String token) {return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(getKey()).build().parseSignedClaims(token).getPayload();
    }

    public Long extractIdFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return Long.parseLong(extractUserName(authHeader.substring(7)));
        }
        throw new RuntimeException("Missing or invalid token");
    }


}
