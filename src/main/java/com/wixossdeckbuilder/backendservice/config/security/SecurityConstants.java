package com.wixossdeckbuilder.backendservice.config.security;

public interface SecurityConstants {
    public static final String JWT_KEY = "shouldProbablyChangeThisLater";
    public static final String JWT_HEADER = "Authorization";
    public static final int JWT_EXP_IN_MS = 7200000;
}
