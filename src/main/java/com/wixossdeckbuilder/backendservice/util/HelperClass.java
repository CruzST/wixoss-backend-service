package com.wixossdeckbuilder.backendservice.util;

import com.wixossdeckbuilder.backendservice.model.payloads.LoginRequest;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class HelperClass {

    public static LoginRequest decodeAuthHeader(String userNamePwString) {
        byte[] decodedBytes = Base64.getDecoder().decode(userNamePwString);
        String decodedString = new String(decodedBytes);
        String[] stringArr = decodedString.split(":");
        return new LoginRequest(stringArr[0], stringArr[1]);
    }
}
