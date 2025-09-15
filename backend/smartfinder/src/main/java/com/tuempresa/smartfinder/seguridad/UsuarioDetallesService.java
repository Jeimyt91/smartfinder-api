package com.tuempresa.smartfinder.seguridad;

import com.tuempresa.smartfinder.dominio.Usuario;
import com.tuempresa.smartfinder.repositorio.UsuarioRepo;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class UsuarioDetallesService implements UserDetailsService {

  private final UsuarioRepo repo;
  public UsuarioDetallesService(UsuarioRepo repo) { this.repo = repo; }

  @Override
  public UserDetails loadUserByUsername(String userOrEmail) throws UsernameNotFoundException {
    Usuario u = repo.findByUsernameIgnoreCase(userOrEmail)
      .orElseGet(() -> repo.findByEmailIgnoreCase(userOrEmail)
        .orElseThrow(() -> new UsernameNotFoundException("No existe usuario: " + userOrEmail)));

    return User.withUsername(u.getUsername() != null ? u.getUsername() : u.getEmail())
      .password(u.getPassword())
      .roles(u.getRol().name()) // 👈 aquí se usa getRol()
      .build();
  }
}