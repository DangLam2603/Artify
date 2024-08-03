package com.example.artworksharingplatform.service.JWTServices;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTService {

    public static final String SECURE_KEY = "994de78c709e08065f0ed77c880c8a3602e18359e36cfde7d256166b6fb12ece";

    public String GetUserEmailJWT(String token) {
        return exactClaim(token, Claims::getSubject);
    }

    public <T> T exactClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = GetAllToken(token);
        return claimsResolver.apply(claims);
    }

    public Claims GetAllToken(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignKey() {
        byte[] keyByte = Decoders.BASE64.decode(SECURE_KEY);
        return Keys.hmacShaKeyFor(keyByte);
    }

    public String generateToken(Map<String, Object> extractClaim, UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(extractClaim)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 10000 * 60 * 24))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(), userDetails);
    }
    //check validate token
    public boolean isTokenValid(String token, UserDetails userDetails){
        final String email = GetUserEmailJWT(token);
        return (email.equals(userDetails.getUsername())) && !isTokenExpried(token);
    }

    private boolean isTokenExpried(String token) {
        return extractExpirationToken(token).before(new Date());
    }

    private Date extractExpirationToken(String token) {
        return exactClaim(token,Claims::getExpiration);
    }
}
