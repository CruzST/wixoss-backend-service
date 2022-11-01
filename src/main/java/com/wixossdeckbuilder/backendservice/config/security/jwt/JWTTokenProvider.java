package com.wixossdeckbuilder.backendservice.config.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import org.slf4j.Logger;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class JWTTokenProvider {

    @Value("${jwt.key}")
    private String JWT_KEY;

    @Value("${jwt.exp.ms}")
    private int JWT_EXP_MS;

    @Value("${jwt.exp.hr}")
    private int JWT_EXP_HR;

    public static final Logger logger = LoggerFactory.getLogger(JWTTokenProvider.class);

    public String generateToken(Authentication authentication) {
        SecretKey key = Keys.hmacShaKeyFor(JWT_KEY.getBytes(StandardCharsets.UTF_8));

        /* Method 2 to adding time to expiration date */
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR_OF_DAY, JWT_EXP_HR);

        String jwt = Jwts.builder()
                .setIssuer("Cruz")
                .setSubject("Authentication token for User") // change later?
                .claim("username", authentication.getName())
                .claim("authorities", populateAuth(authentication.getAuthorities()))
                .setIssuedAt(new Date())
                //.setExpiration(new Date(new Date().getTime() + JWT_EXP_MS)) // Method 1
                .setExpiration(calendar.getTime()) // Method 2
                .signWith(key).compact();
        return jwt;
    }

    private String populateAuth(Collection<? extends GrantedAuthority> collection) {
        Set<String> authoritiesSet = new HashSet<>();
        for (GrantedAuthority auth: collection) {
            authoritiesSet.add(auth.getAuthority());
        }
        return String.join(",", authoritiesSet);
    }

    public Claims validateToken(String authToken){
        try{
            SecretKey key = Keys.hmacShaKeyFor(JWT_KEY.getBytes(StandardCharsets.UTF_8));
            Claims claims = Jwts.parserBuilder().setSigningKey(key)
                    .build()
                    .parseClaimsJws(authToken)// this will parse and check the if it is valid
                    .getBody();
            return claims;
        } catch (SignatureException ex) {
            logger.error("Invalid JWT signature");
        } catch (MalformedJwtException ex){
            logger.error("Invalid JWT token");
        } catch (ExpiredJwtException ex){
            logger.error("Expired JWT token");
        } catch (UnsupportedJwtException ex){
            logger.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty");
        }
        return null;
    }
}
