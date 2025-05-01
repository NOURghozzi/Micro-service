package com.userService.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
@Slf4j
@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long jwtExpirationInMs;

    @Value("${jwt.token.prefix}")
    private String tokenPrefix;

    @Value("${jwt.header.string}")
    private String headerString;

    public String generateToken(String username, Collection<? extends GrantedAuthority> authorities,String role,Long id) {
        Set<String> permissions = authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
        return Jwts.builder()
                .setSubject(username)
                .claim("authorities", permissions) // Add roles or other claims as needed
                .claim("role", role) // Add roles or other claims as needed
                .claim("id", id) // Add roles or other claims as needed
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationInMs))
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes())
                .compact();
    }
    private String getTokenFromContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getCredentials() != null) {
            // Add debug statement
            log.info("Extracted token from context: " + authentication.getCredentials().toString());
            return authentication.getCredentials().toString();
        }
        return null;
    }
    private Map<String, Object> getJwtFromToken() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        if (securityContext != null) {
            Authentication authentication = securityContext.getAuthentication();
            if (authentication != null && authentication.getCredentials() != null) {
                String jwtToken = authentication.getCredentials().toString();
                Claims claims = Jwts.parser()
                        .setSigningKey(secretKey)
                        .parseClaimsJws(jwtToken)
                        .getBody();
                return claims;
            }
        }
        return null;
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }


    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(secretKey.getBytes()).parseClaimsJws(token).getBody();
    }

    public Boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public Collection<? extends GrantedAuthority> getAuthorities(String token) {
        Claims claims = extractAllClaims(token);
        List<String> authorities = claims.get("authorities", List.class);
        return authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    public String getRole() {
        String token = getTokenFromContext();
        Claims claims = extractAllClaims(token);
        return (String) claims.get("role");
    }

    public Long getUserId() {
        String token = getTokenFromContext();
        Claims claims = extractAllClaims(token);
        return ((Integer) claims.get("id")).longValue(); // Convert to Long if necessary
    }
}