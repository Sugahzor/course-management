package com.nexttech.coursemanagement.security;

import com.nexttech.coursemanagement.models.AppUserPrincipal;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtUtil {
    private static final long EXPIRE_DURATION = 60 * 60 * 1000; // 1h
    @Value("${app.jwt.secret}")
    private String SECRET_KEY;

    public String generateAccessToken(AppUserPrincipal user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("roles", authoritiesToString(user.getAuthorities()))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_DURATION))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    private String authoritiesToString(Collection<? extends GrantedAuthority> authorities) {
        return authorities
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
    }

    private Collection<? extends GrantedAuthority> authoritiesFromString(String authorities) {
        return AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);
    }

    public boolean validateAccessToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        }
        catch (ExpiredJwtException ex) {
            log.error("JWT expired", ex);
        }
        catch(IllegalArgumentException ex) {
            log.error("Token is null, empty or only whitespace", ex);
        }
        catch (MalformedJwtException ex) {
            log.error("JWT is invalid", ex);
        }
        catch (UnsupportedJwtException ex) {
            log.error("JWT is not supported", ex);
        }
        catch (SignatureException ex) {
            log.error("Signature validation failed", ex);
        }

        return false;
    }

    public String getSubject(String token) {
        return parseClaims(token).getSubject();
    }

    public Claims parseClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

//    public Collection<GrantedAuthority> getGrantedAuthorities(String token) {
//        return authoritiesFromString((String) parseClaims(token).get("roles"));
//    }

}
