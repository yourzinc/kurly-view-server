package com.kurly.kurlyview.security.jwt;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

public class UserAuthentication  extends UsernamePasswordAuthenticationToken {
    public UserAuthentication(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public UserAuthentication(String principal, String credentials,
                              List<GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }
}
