package com.example.skarte.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.skarte.entity.User;
import com.example.skarte.repository.UserRepository;

@Component
public class FormAuthenticationProvider implements AuthenticationProvider {

    protected static Logger log = LoggerFactory.getLogger(FormAuthenticationProvider.class);
    @Autowired
    private UserRepository repository;
    @Lazy
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException {
        String userId = auth.getName();
        String password = auth.getCredentials().toString();

        log.debug("getCredentials={}", auth.getCredentials().toString());
        log.debug("getPrincipal={}", auth.getPrincipal().toString());
        log.debug("userId={}", userId);
        log.debug("password={}", password);

        if ("".equals(userId) || "".equals(password)) {
            throw new AuthenticationCredentialsNotFoundException("ログイン情報に不備があります。");
        }

        User entity = repository.findByUserId(userId);
        if (entity == null) {
            throw new AuthenticationCredentialsNotFoundException("ログイン情報が存在しません。");
        }

        if (!passwordEncoder.matches(password, entity.getPassword())) {
            throw new AuthenticationCredentialsNotFoundException("ログイン情報に不備があります。");
        }

        return new UsernamePasswordAuthenticationToken(entity, password, entity.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}