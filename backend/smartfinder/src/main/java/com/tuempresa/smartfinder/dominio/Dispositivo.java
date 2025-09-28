package com.tuempresa.smartfinder.dominio;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Entity
@Table(name = "dispositivo")
public class Dispositivo {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  @Size(max = 120)
  private String nombre;

  @NotBlank
  @Size(max = 80)
  private String marca;

  @NotBlank
  @Size(max = 40)
  private String tipo;

  @NotNull
  @DecimalMin("0.0")
  private BigDecimal precio;

  @NotNull
  private Boolean disponible = true;

  // --- Getters y Setters ---
  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }

  public String getNombre() { return nombre; }
  public void setNombre(String nombre) { this.nombre = nombre; }

  public String getMarca() { return marca; }
  public void setMarca(String marca) { this.marca = marca; }

  public String getTipo() { return tipo; }
  public void setTipo(String tipo) { this.tipo = tipo; }

  public BigDecimal getPrecio() { return precio; }
  public void setPrecio(BigDecimal precio) { this.precio = precio; }

  public Boolean getDisponible() { return disponible; }
  public void setDisponible(Boolean disponible) { this.disponible = disponible; }
}
