/**
 *  *
 *  * // This file is no longer needed with the addition of the CustomAuthenticationProvider class
package com.wixossdeckbuilder.backendservice.config.security;

import com.wixossdeckbuilder.backendservice.model.entities.WixossUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class UserPrincipal  implements UserDetails {
    private WixossUser wixossUser;
    private Set<GrantedAuthority> authorities;

    public UserPrincipal(WixossUser wixossUser) {
        super();
        this.wixossUser = wixossUser;
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(wixossUser.getUserRole()));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return wixossUser.getUserPassword();
    }

    @Override
    public String getUsername() {
        return wixossUser.getUserName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return wixossUser.getUserEnabled();
    }

    @Override
    public boolean isAccountNonLocked() {
        return wixossUser.getUserEnabled();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return wixossUser.getUserEnabled();
    }

    @Override
    public boolean isEnabled() {
        return wixossUser.getUserEnabled();
    }
}
**/