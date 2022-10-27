/**
 *
 * // This file is no longer needed with the addition of the CustomAuthenticationProvider class
package com.wixossdeckbuilder.backendservice.config.security;

import com.wixossdeckbuilder.backendservice.model.entities.WixossUser;
import com.wixossdeckbuilder.backendservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomUserDetails implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        WixossUser wixossUser = userRepository.findByEmail(username);
        if (wixossUser == null) {
            throw new UsernameNotFoundException("User not found. 404");
        }
        return new UserPrincipal(wixossUser);
    }

    public UserDetails loadUserById(Long id) {
        WixossUser wixossUser = userRepository.findById(id).orElseThrow(
                () -> new UsernameNotFoundException("User not found with id : " + id)
        );
        return new UserPrincipal(wixossUser);
    }
}
**/