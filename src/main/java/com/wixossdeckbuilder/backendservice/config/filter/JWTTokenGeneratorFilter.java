package com.wixossdeckbuilder.backendservice.config.filter;

import com.wixossdeckbuilder.backendservice.config.security.SecurityConstants;
import com.wixossdeckbuilder.backendservice.config.security.jwt.JWTTokenProvider;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class JWTTokenGeneratorFilter extends OncePerRequestFilter {

    @Autowired
    JWTTokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            /*
            SecretKey key = Keys.hmacShaKeyFor(SecurityConstants.JWT_KEY.getBytes(StandardCharsets.UTF_8));
            String jwt = Jwts.builder()
                    .setIssuer("Cruz")
                    .setSubject("Authentication token for User") // change later?
                    .claim("username", authentication.getName())
                    .claim("authorities", populateAuth(authentication.getAuthorities()))
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(new Date().getTime() + SecurityConstants.JWT_EXP_IN_MS)) // 2hrs
                    .signWith(key).compact();
             */
            String jwt = tokenProvider.generateToken(authentication);
            response.setHeader(SecurityConstants.JWT_HEADER, jwt);
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !request.getServletPath().equals("/api/auth/login"); // This will change to be the path that does my login
    }

    // Not needed if using tokenProvider
    private String populateAuth(Collection<? extends GrantedAuthority> collection) {
        Set<String> authoritiesSet = new HashSet<>();
        for (GrantedAuthority auth: collection) {
            authoritiesSet.add(auth.getAuthority());
        }
        return String.join(",", authoritiesSet);
    }
}
