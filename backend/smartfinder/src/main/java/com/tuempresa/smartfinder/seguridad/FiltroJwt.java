package com.tuempresa.smartfinder.seguridad;

import java.io.IOException;
import java.util.Optional;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Lee el header Authorization: Bearer <jwt>
 * Valida el token y, si es válido, setea la autenticación en el contexto.
 */
@Component
public class FiltroJwt extends OncePerRequestFilter {

  private final ServicioJwt servicioJwt;
  private final UserDetailsService servicioUsuarios;

  public FiltroJwt(ServicioJwt servicioJwt, UserDetailsService servicioUsuarios) {
    this.servicioJwt = servicioJwt;
    this.servicioUsuarios = servicioUsuarios;
  }

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain chain) throws ServletException, IOException {

    String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      chain.doFilter(request, response);
      return;
    }

    String token = authHeader.substring(7);
    Optional<String> correoOpt = servicioJwt.obtenerCorreo(token);

    if (correoOpt.isEmpty() || SecurityContextHolder.getContext().getAuthentication() != null) {
      chain.doFilter(request, response);
      return;
    }

    String correo = correoOpt.get();
    UserDetails usuario = servicioUsuarios.loadUserByUsername(correo);

    if (servicioJwt.esValido(token, usuario)) {
      UsernamePasswordAuthenticationToken authToken =
          new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
      authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
      SecurityContextHolder.getContext().setAuthentication(authToken);
    }

    chain.doFilter(request, response);
  }
}
