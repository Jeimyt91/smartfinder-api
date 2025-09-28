package com.tuempresa.smartfinder.configuracion;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.*;

@Configuration
public class ConfiguracionCors {

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration cfg = new CorsConfiguration();
    // Orígenes permitidos: pon solo los que realmente uses
    cfg.setAllowedOrigins(List.of(
        "http://localhost:5500",
        "http://127.0.0.1:5500",
        "http://localhost:8080",
        "http://localhost:3000"
    ));
    // Métodos
    cfg.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
    // Headers
    cfg.setAllowedHeaders(List.of("*"));
    // Si necesitas leer cookies/autorización desde el front:
    cfg.setAllowCredentials(true);
    // (Opcional) Exponer headers personalizados al navegador
    // cfg.setExposedHeaders(List.of("Authorization", "Location"));

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", cfg);
    return source;
  }
}
