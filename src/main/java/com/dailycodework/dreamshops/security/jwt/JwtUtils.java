package com.dailycodework.dreamshops.security.jwt;

import java.util.Date;
import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import com.dailycodework.dreamshops.security.user.ShopUser;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

@Component
public class JwtUtils {
  @Value("${auth.token.jwtSecret}")
  private String jwtSecret;
  @Value("${auth.token.expirationInMils}")
  private int expirationTime;

  public String generateTokenForUser(Authentication authentication) {
    ShopUser userPrincipal = (ShopUser) authentication.getPrincipal();

    List<String> roles = userPrincipal.getAuthorities().stream().map(GrantedAuthority::getAuthority)
        .toList();

    return Jwts.builder()
        .subject(userPrincipal.getUsername())
        .claim("roles", roles)
        .issuedAt(new Date())
        .expiration(new Date((new Date()).getTime() + expirationTime))
        .signWith(key())
        .compact();
  }

  public SecretKey key() {
    return Jwts.SIG.HS512.key().build();
  }

  public String getUsernameFromToken(String token) {
    return Jwts.parser().verifyWith(key()).build().parseSignedClaims(token).getPayload().getSubject();
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parser().verifyWith(key()).build().parseSignedClaims(token);
      return true;
    } catch (JwtException | IllegalArgumentException e) {
      throw new JwtException(e.getMessage());
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }
  }

}
