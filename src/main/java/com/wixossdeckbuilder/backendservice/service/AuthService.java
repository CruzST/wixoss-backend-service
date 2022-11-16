package com.wixossdeckbuilder.backendservice.service;
import com.wixossdeckbuilder.backendservice.config.CustomAuthenticationProvider;
import com.wixossdeckbuilder.backendservice.config.security.jwt.JWTTokenProvider;
import com.wixossdeckbuilder.backendservice.model.payloads.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private CustomAuthenticationProvider customAuthenticationProvider;

    @Autowired
    private JWTTokenProvider jwtTokenProvider;

    public String authenticateUser(Authentication auth) throws BadCredentialsException {
        try {
            Authentication loginToken = customAuthenticationProvider.authenticate(auth);
            SecurityContextHolder.getContext().setAuthentication(loginToken);
            String jwt = jwtTokenProvider.generateToken(loginToken);
            return jwt;
        } catch (BadCredentialsException e) {
            throw e;
        }
    }
}
