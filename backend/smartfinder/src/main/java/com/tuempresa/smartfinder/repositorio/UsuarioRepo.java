package com.tuempresa.smartfinder.repositorio;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.tuempresa.smartfinder.dominio.Usuario;

public interface UsuarioRepo extends JpaRepository<Usuario, UUID> {
  Optional<Usuario> findByUsernameIgnoreCase(String username);
  Optional<Usuario> findByEmailIgnoreCase(String email);
  boolean existsByUsernameIgnoreCase(String username);
  boolean existsByEmailIgnoreCase(String email);
}
