package com.wixossdeckbuilder.backendservice.controller;


import com.wixossdeckbuilder.backendservice.model.entities.WixossUser;
import com.wixossdeckbuilder.backendservice.model.payloads.AuthPayload;
import com.wixossdeckbuilder.backendservice.model.payloads.LoginRequest;
import com.wixossdeckbuilder.backendservice.model.payloads.UserRequest;
import com.wixossdeckbuilder.backendservice.service.AuthService;
import com.wixossdeckbuilder.backendservice.service.UserService;
import com.wixossdeckbuilder.backendservice.util.Constants;
import com.wixossdeckbuilder.backendservice.util.HelperClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    public static final Logger logger = LoggerFactory.getLogger(AuthController.class);

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
        // Need to add validation for certain chars
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
            HttpStatus code = HttpStatus.UNPROCESSABLE_ENTITY;
            if (e.getMessage().equals(Constants.USER_ALREADY_EXISTS)) {
                code = HttpStatus.CONFLICT;
            } else if (e.getMessage().equals(Constants.ILLEGAL_USER_NAME)) {
                code = HttpStatus.BAD_REQUEST;
            }
            response = ResponseEntity.status(code).body(e.getMessage());
        }
        return response;
    }

    @GetMapping("/login")
    public ResponseEntity<AuthPayload> authenticateuser(HttpServletRequest request) {
        String userNamePassword = request.getHeader("Authorization");
        LoginRequest loginRequest = HelperClass.decodeAuthHeader(userNamePassword);
        Authentication loginToken = new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());
        ResponseEntity response = null;
        try {
            String jwt = authService.authenticateUser(loginToken);
            response = ResponseEntity.ok(new AuthPayload(loginRequest.getEmail(), jwt));
        } catch (BadCredentialsException e) {
            response = (e.getMessage().equals(Constants.USER_NOT_FOUND)) ?
                    ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()) :
                    ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
        return response;
    }

    /* Login end point for basic auth with formlogin*/
    @GetMapping("/login/{userEmail}")
    public ResponseEntity<WixossUser> authenticateuser(@PathVariable(value = "userEmail") String email) {
        Optional<WixossUser> user = userService.getUserByEmail(email);
        ResponseEntity response = null;
        if (user.isPresent()) {
            response = ResponseEntity.ok(user.get());
        } else {
            response = ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return response;
    }
}
