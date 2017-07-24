/*
 * Copyright (c) 2010-2017 BuildingIQ Pty Limited. All rights reserved.
 * 
 * The Intellectual Property Rights of BuildingiQ Pty Limited in this source
 * code and the BuildingIQ software are protected under Australian and
 * international copyright law. Unauthorised reproduction, distribution or use
 * of this software source code or any portion of either of them is expressly
 * prohibited and may result in severe civil or criminal penalties.
 */
package com.allan.sso.config.security.providers;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class LegacyAuthenticationProvider implements AuthenticationProvider{
    
    static final Logger l = LoggerFactory.getLogger(LegacyAuthenticationProvider.class);

    static final String[][] USER_LIST = {
            {"admin", "adm2n", "ADMIN"},
            {"bbtest", "testerb", "USER"}
    };
    
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        List<GrantedAuthority> roles = validateUser(username, password);
        
        l.info("Authenticating {} with credentials {}.", username, password);
        
        if(roles == null) {
            throw new BadCredentialsException("Invalid username/password");
        }
        
        return new UsernamePasswordAuthenticationToken(username, password, roles);
    }
    
    private List<GrantedAuthority> validateUser(String username, String password) {
        for(String[] user : USER_LIST) {
            if(user[0].equals(username) && user[1].equals(password)) {
                //Build the roles
                List<GrantedAuthority> roles = new ArrayList<>();
                String[] roleList = user[2].split(",");
                for(String role : roleList) {
                    roles.add(new SimpleGrantedAuthority("ROLE_" + role));
                }
                
                return roles;
            }
        }
        
        return null; // Authentication failed
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
