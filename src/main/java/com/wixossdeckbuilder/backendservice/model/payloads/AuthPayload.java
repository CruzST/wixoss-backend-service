package com.wixossdeckbuilder.backendservice.model.payloads;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class AuthPayload {
    private String email;
    private String token;
}
