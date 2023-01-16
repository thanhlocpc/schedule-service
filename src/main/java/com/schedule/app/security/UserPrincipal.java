package com.schedule.app.security;

import lombok.Data;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * @author : Thành Lộc
 * @since : 10/12/2022, Wed
 **/

@Data
public class UserPrincipal implements UserDetails {
    private Long userId;
    private String username;
    private String password;
    private Collection authorities;

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
