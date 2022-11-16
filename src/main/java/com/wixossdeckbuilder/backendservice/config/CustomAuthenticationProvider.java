package com.wixossdeckbuilder.backendservice.config;

import com.wixossdeckbuilder.backendservice.model.enums.CustomRole;
import com.wixossdeckbuilder.backendservice.model.entities.WixossUser;
import com.wixossdeckbuilder.backendservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();
        WixossUser wixossUser = userRepository.findByEmail((email));
        if (Objects.nonNull(wixossUser)) {
            if (passwordEncoder.matches(password, wixossUser.getPassword())) {
                return new UsernamePasswordAuthenticationToken(email, password, getGrantedAuthorities(wixossUser.getAuthorities()));
            } else {
                throw new BadCredentialsException("Authentication exception: Invalid password provided!");
            }
        } else {
            throw new BadCredentialsException("Authentication exception: No user found with provided details!");
        }
    }

    private List<GrantedAuthority> getGrantedAuthorities(Set<CustomRole> authorities) {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (CustomRole auth : authorities) {
            grantedAuthorities.add(new SimpleGrantedAuthority(auth.toString()));
        }
        return grantedAuthorities;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
