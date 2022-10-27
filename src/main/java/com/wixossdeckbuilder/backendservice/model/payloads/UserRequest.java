package com.wixossdeckbuilder.backendservice.model.payloads;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserRequest {
    private String username;
    private String userEmail;
    private String userPassword;
}
