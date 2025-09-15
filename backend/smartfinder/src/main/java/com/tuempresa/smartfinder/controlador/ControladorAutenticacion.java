package com.tuempresa.smartfinder.controlador;

import java.util.Map;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.tuempresa.smartfinder.dto.*;
import com.tuempresa.smartfinder.dominio.Rol;      // enum
import com.tuempresa.smartfinder.dominio.Usuario; // tu entidad
import com.tuempresa.smartfinder.repositorio.UsuarioRepo;
import com.tuempresa.smartfinder.seguridad.ServicioJwt;

@RestController
@RequestMapping("/api/auth")
public class ControladorAutenticacion {

  private final AuthenticationManager authManager;
  private final PasswordEncoder encoder;
  private final UsuarioRepo usuarioRepo;
  private final ServicioJwt jwt;

  public ControladorAutenticacion(AuthenticationManager authManager, PasswordEncoder encoder,
                        UsuarioRepo usuarioRepo, ServicioJwt jwt) {
    this.authManager = authManager;
    this.encoder = encoder;
    this.usuarioRepo = usuarioRepo;
    this.jwt = jwt;
  }

  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody RegisterDTO in) {
    String id = in.getEmailOrUsername().trim();

    // si digitó un email, guárdalo en email; si no, como username
    boolean esEmail = id.contains("@");
    if (esEmail && usuarioRepo.existsByEmailIgnoreCase(id)) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error","Email ya existe"));
    }
    if (!esEmail && usuarioRepo.existsByUsernameIgnoreCase(id)) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error","Username ya existe"));
    }

    Usuario u = new Usuario();
    if (esEmail) u.setEmail(id.toLowerCase());   // 
    else u.setUsername(id);
    u.setPassword(encoder.encode(in.getPassword()));
   
    u.setRol(Rol.ADMIN);

    usuarioRepo.save(u);
    return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "Admin creado"));
  
  }

  @PostMapping("/login")
  public ResponseEntity<TokenDTO> login(@RequestBody LoginDTO in) {
    Authentication auth = authManager.authenticate(
        new UsernamePasswordAuthenticationToken(in.getEmailOrUsername(), in.getPassword())
    );
    UserDetails principal = (UserDetails) auth.getPrincipal();
    String token = jwt.emitirToken(principal);  
    return ResponseEntity.ok(new TokenDTO(token));
  }
}
