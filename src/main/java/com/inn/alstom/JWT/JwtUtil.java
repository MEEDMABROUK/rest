package com.inn.alstom.JWT;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Logger;
@Service
public class JwtUtil {
    private static final Logger log = Logger.getLogger(JwtUtil.class.getName());
    private String secret = "MERYEM";
    public String extractUsername(String token){
        log.info("Extracting username from JWT token: " +token);
        return extractClaims(token,Claims::getSubject);
    }
    public Date extractExpiration(String token){
        return extractClaims(token,Claims::getExpiration);
    }
    public <T> T extractClaims(String token, Function<Claims,T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);

    }
    public Claims extractAllClaims(String token){
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }
    private Boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }
    public Boolean validateToken(String token,UserDetails userDetails){
        final String username=extractUsername(token);
        log.info("validating token " + token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
    public String generateToken(String username,String role){
        Map<String,Object> claims = new HashMap<>();
        claims.put("role",role);
        return createToken(claims,username);
    }

    private String createToken(Map<String,Object> claims ,String subject){
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date((System.currentTimeMillis())))
                .setExpiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 365))
                .signWith(SignatureAlgorithm.HS256,secret).compact();


    }


}
