package com.tuempresa.smartfinder.controlador;
import com.tuempresa.smartfinder.repositorio.UsuarioRepo;
import com.tuempresa.smartfinder.dominio.Usuario;
import com.tuempresa.smartfinder.dominio.Rol;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import com.tuempresa.smartfinder.dto.RegisterDTO;


import org.springframework.security.config.Customizer;
@RestController
@RequestMapping("/api/auth")
public class ControladorAutenticacion {

  private final UsuarioRepo usuarioRepo;
  private final PasswordEncoder encoder;

  public ControladorAutenticacion(UsuarioRepo usuarioRepo, PasswordEncoder encoder) {
    this.usuarioRepo = usuarioRepo;
    this.encoder = encoder;
  }

 @PostMapping("/register")
public ResponseEntity<?> register(@RequestBody RegisterDTO in) {
  String id = in.getEmailOrUsername() == null ? "" : in.getEmailOrUsername().trim();
  if (id.isEmpty() || in.getPassword() == null || in.getPassword().isBlank()) {
    return ResponseEntity.badRequest().body(Map.of("error", "emailOrUsername y password son obligatorios"));
  }

  boolean esEmail = id.contains("@");

  if (esEmail && usuarioRepo.existsByCorreoIgnoreCase(id)) {
    return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "Email ya existe"));
  }
  if (!esEmail && usuarioRepo.existsByNombreIgnoreCase(id)) {
    return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "Username ya existe"));
  }

  Usuario u = new Usuario();
  if (esEmail) {
    String local = id.substring(0, id.indexOf('@'));
    u.setCorreo(id);
    u.setNombre(local);
    u.setUsername(local);              // <- IMPORTANTE
  } else {
    u.setNombre(id);
    u.setUsername(id);
    // si decides permitir registro por username sin email, vuelve nullable el correo en la entidad
    // o solicita correo.
  }

  u.setPassword(encoder.encode(in.getPassword())); // nunca en claro
  u.setRol(Rol.ADMIN);

  usuarioRepo.save(u);
  return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "Admin creado"));
}
}

