package com.wixossdeckbuilder.backendservice.config.security.jwt;

import com.wixossdeckbuilder.backendservice.config.security.SecurityConstants;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;

import javax.crypto.SecretKey;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class JWTTokenProvider {

    public static final Logger logger = LoggerFactory.getLogger(JWTTokenProvider.class);

    private SecretKey key = Keys.hmacShaKeyFor(SecurityConstants.JWT_KEY.getBytes(StandardCharsets.UTF_8));

    public String generateToken(Authentication authentication) {
        String jwt = Jwts.builder()
                .setIssuer("Cruz")
                .setSubject("Authentication token for User") // change later?
                .claim("username", authentication.getName())
                .claim("authorities", populateAuth(authentication.getAuthorities()))
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + SecurityConstants.JWT_EXP_IN_MS)) // 2hrs
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
