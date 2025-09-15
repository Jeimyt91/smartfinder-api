package com.tuempresa.smartfinder.seguridad;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.*;

import io.jsonwebtoken.security.Keys;

@Service
public class ServicioJwt {

  @Value("${seguridad.jwt.secreto:esta_es_una_clave_super_secreta_de_desarrollo_1234567890_esta_es_una}")
  private String secreto;

  @Value("${seguridad.jwt.vigencia_ms:86400000}")
  private long vigenciaMs;

  private SecretKey clave() {
    return Keys.hmacShaKeyFor(secreto.getBytes(StandardCharsets.UTF_8));
  }

  public String emitirToken(UserDetails usuario) { return emitirToken(usuario, Map.of()); }

  public String emitirToken(UserDetails usuario, Map<String,Object> extras) {
    Date ahora = new Date();
    Date expira = new Date(ahora.getTime() + vigenciaMs);

    return Jwts.builder()
        .setClaims(extras)
        .setSubject(usuario.getUsername())
        .setIssuedAt(ahora)
        .setExpiration(expira)
        .signWith(clave(), SignatureAlgorithm.HS256) // 0.11.x
        .compact();
  }

  // Alias
  public String generateToken(UserDetails usuario) { return emitirToken(usuario); }
  public String generateToken(Map<String,Object> extras, UserDetails usuario) { return emitirToken(usuario, extras); }

  public Optional<String> obtenerCorreo(String token) {
    try {
      Claims c = Jwts.parserBuilder()
          .setSigningKey(clave()) // 0.11.x
          .build()
          .parseClaimsJws(token)
          .getBody();
      return Optional.ofNullable(c.getSubject());
    } catch (JwtException | IllegalArgumentException e) {
      return Optional.empty();
    }
  }

  public boolean esValido(String token, UserDetails usuario) {
    try {
      Claims c = Jwts.parserBuilder()
          .setSigningKey(clave())
          .build()
          .parseClaimsJws(token)
          .getBody();
      return usuario.getUsername().equals(c.getSubject()) &&
             c.getExpiration() != null &&
             c.getExpiration().after(new Date());
    } catch (JwtException | IllegalArgumentException e) {
      return false;
    }
  }
}
