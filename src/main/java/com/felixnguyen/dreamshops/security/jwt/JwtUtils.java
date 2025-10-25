package com.felixnguyen.dreamshops.security.jwt;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import com.felixnguyen.dreamshops.security.user.ShopUserDetails;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;

@Component
public class JwtUtils {

  @Value("${auth.token.jwt-secret}")
  private String jwtSecret;

  @Value("${auth.token.expiration-ms}")
  private int jwtExpirationTime;

  public String generateTokenForUser(Authentication authentication) {
    ShopUserDetails userPrincipal = (ShopUserDetails) authentication.getPrincipal();
    List<String> roles = userPrincipal.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.toList());

    return Jwts.builder()
        .setSubject(userPrincipal.getUsername())
        .claim("id", userPrincipal.getId())
        .claim("roles", roles)
        .setIssuedAt(new Date())
        .setExpiration(new Date((new Date()).getTime() + jwtExpirationTime))
        .signWith(key(), SignatureAlgorithm.HS512)
        .compact();
  }

  private Key key() {
    return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
  }

  public String getUsernameFromToken(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(key())
        .build()
        .parseClaimsJws(token)
        .getBody()
        .getSubject();
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(token);
      return true;
    } catch (JwtException | IllegalArgumentException e) {
      throw new JwtException(e.getMessage(), e);
    }
  }
}
