package com.wixossdeckbuilder.backendservice.controller;

import com.wixossdeckbuilder.backendservice.config.CustomAuthenticationProvider;
import com.wixossdeckbuilder.backendservice.config.security.jwt.JWTTokenProvider;
import com.wixossdeckbuilder.backendservice.model.entities.WixossUser;
import com.wixossdeckbuilder.backendservice.model.payloads.AuthPayload;
import com.wixossdeckbuilder.backendservice.model.payloads.LoginRequest;
import com.wixossdeckbuilder.backendservice.model.payloads.UserRequest;
import com.wixossdeckbuilder.backendservice.service.AuthService;
import com.wixossdeckbuilder.backendservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthService authService;



    @PostMapping("/register")
    ResponseEntity<WixossUser> createNewUser(@RequestBody @Valid UserRequest userRequest) {
        ResponseEntity response = null;
        WixossUser savedWixossUser = null;
        System.out.println("calling the controller");
        try {
            String hashedPwd = passwordEncoder.encode(userRequest.getUserPassword());
            userRequest.setUserPassword(hashedPwd);
            savedWixossUser = userService.createNewUser(userRequest);
            if (savedWixossUser.getId() > 0) {
                response = ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(savedWixossUser);
            }
        } catch (Exception e) {
            response = ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An exception occurred during user register: " + e.getMessage());

        }
        return response;
    }

    @GetMapping("/login")
    public ResponseEntity<AuthPayload> authenticateuser(@RequestBody @Valid LoginRequest loginRequest) {
        Authentication loginToken = new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());
        ResponseEntity response = null;
        try {
            String jwt = authService.authenticateUser(loginToken);
            response = ResponseEntity.ok(new AuthPayload(loginRequest.getEmail(), jwt));
        } catch (BadCredentialsException e) {
            response = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
        return response;
    }
}
