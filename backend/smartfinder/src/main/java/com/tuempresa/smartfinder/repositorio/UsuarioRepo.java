package com.tuempresa.smartfinder.repositorio;

import com.tuempresa.smartfinder.dominio.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepo extends JpaRepository<Usuario, UUID> {
  // para validaciones de duplicados
    boolean existsByCorreoIgnoreCase(String correo);
    boolean existsByNombreIgnoreCase(String nombre);

    // para login / búsqueda
    Optional<Usuario> findByCorreoIgnoreCase(String correo);
    Optional<Usuario> findByNombreIgnoreCase(String nombre);
}
