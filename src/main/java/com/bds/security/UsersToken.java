package com.bds.security;

import com.bds.models.Users;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Collection;

public class UsersToken extends JwtAuthenticationToken {
    private static final long serialVersionUID = 1L;
    private final Users user;

    public UsersToken(Jwt jwt, Collection<? extends GrantedAuthority> authorities, String name, Users user) {
        super(jwt, authorities, name);
        this.user = user;
    }

    public Users getAccount() {
        return user;
    }
}
