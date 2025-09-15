package com.tuempresa.smartfinder.dominio;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "usuarios")
public class Usuario {

  @Id
  @GeneratedValue
  private UUID id;

  @Column(unique = true)
  private String username;

  @Column(unique = true)
  private String email;

  @Column(nullable = false)
  private String password;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Rol rol; // 👈 campo necesario

  // --- getters/setters ---

  public UUID getId() { return id; }
  public void setId(UUID id) { this.id = id; }

  public String getUsername() { return username; }
  public void setUsername(String username) { this.username = username; }

  public String getEmail() { return email; }
  public void setEmail(String email) { this.email = email; }

  public String getPassword() { return password; }
  public void setPassword(String password) { this.password = password; }

  public Rol getRol() { return rol; }        // 👈 requerido por UsuarioDetallesService
  public void setRol(Rol rol) { this.rol = rol; }  // 👈 requerido por AuthController
}
