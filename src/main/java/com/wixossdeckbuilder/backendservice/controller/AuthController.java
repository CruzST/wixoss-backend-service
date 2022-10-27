package com.wixossdeckbuilder.backendservice.controller;

import com.wixossdeckbuilder.backendservice.model.entities.WixossUser;
import com.wixossdeckbuilder.backendservice.model.payloads.LoginRequest;
import com.wixossdeckbuilder.backendservice.model.payloads.UserRequest;
import com.wixossdeckbuilder.backendservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

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

    @PostMapping("/login")
    public ResponseEntity<?> authenticateuser(@RequestBody @Valid LoginRequest loginRequest) {
        return  null;
    }
}
