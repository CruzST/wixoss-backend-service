package com.wixossdeckbuilder.backendservice.model.payloads;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Getter
public class LoginRequest {

    @NotBlank
    private String email;

    @NotBlank
    private  String password;
}
