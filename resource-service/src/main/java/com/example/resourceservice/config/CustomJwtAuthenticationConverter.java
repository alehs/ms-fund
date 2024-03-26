package com.example.resourceservice.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class CustomJwtAuthenticationConverter implements Converter<Jwt, JwtAuthenticationToken> {
    private Converter<Jwt, Collection<GrantedAuthority>> jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
    private String principalClaimName = JwtClaimNames.SUB;

    public CustomJwtAuthenticationConverter() {
    }

    @Override
    public JwtAuthenticationToken convert(Jwt source) {
        Collection<GrantedAuthority> authorities = this.jwtGrantedAuthoritiesConverter.convert(source);
        addRoleAuthorities(authorities, source);
        String principalClaimValue = source.getClaimAsString(this.principalClaimName);
        return new JwtAuthenticationToken(source, authorities, principalClaimValue);
    }

    private void addRoleAuthorities(Collection<GrantedAuthority> authorities, Jwt source) {
        for (String role : getRoleAuthorities(source)) {
            authorities.add(new SimpleGrantedAuthority(role));
        }
    }

    @SuppressWarnings("unchecked")
    Collection<String> getRoleAuthorities(Jwt source) {
        Object roleAuthorities = source.getClaim("user-authorities");
        if (roleAuthorities instanceof String) {
            if (StringUtils.hasText((String) roleAuthorities)) {
                return Arrays.asList(((String) roleAuthorities).split(" "));
            }
            return Collections.emptyList();
        }
        if (roleAuthorities instanceof Collection) {
            return (Collection<String>) roleAuthorities;
        }
        return Collections.emptyList();
    }
}
