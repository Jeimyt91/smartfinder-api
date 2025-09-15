package com.tuempresa.smartfinder.configuracion;

import com.tuempresa.smartfinder.seguridad.FiltroJwt; // ← tu filtro JWT en español

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class ConfiguracionSeguridad {

  // Filtro JWT propio
  private final FiltroJwt filtroJwt;

  public ConfiguracionSeguridad(FiltroJwt filtroJwt) {
    this.filtroJwt = filtroJwt;
  }

  @Bean
  public SecurityFilterChain cadenaFiltros(HttpSecurity http) throws Exception {
    http
      .csrf(csrf -> csrf.disable())
      .cors(cors -> {}) // Habilita CORS (puedes tener una clase ConfiguracionCors)
      .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      .authorizeHttpRequests(auth -> auth
        // Endpoints públicos de autenticación (registro/inicio de sesión)
        .requestMatchers("/api/autenticacion/**").permitAll()

        // Lectura pública (GET) para catálogos y detalle
        .requestMatchers(HttpMethod.GET,
          "/api/marcas/**",
          "/api/dispositivos/**",
          "/api/comentarios/**"
        ).permitAll()

        // Cualquier otra operación requiere rol ADMIN
        .anyRequest().hasRole("ADMIN")
      )
      .formLogin(f -> f.disable()) // sin formulario /login
      .httpBasic(b -> b.disable()) // sin Basic Auth
      .addFilterBefore(filtroJwt, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  @Bean
  public PasswordEncoder encriptador() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager manejadorAutenticacion(AuthenticationConfiguration cfg) throws Exception {
    return cfg.getAuthenticationManager();
  }
}
