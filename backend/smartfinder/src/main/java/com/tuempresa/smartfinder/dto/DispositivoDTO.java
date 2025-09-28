package com.tuempresa.smartfinder.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record DispositivoDTO(
  Long id,
  @NotBlank @Size(max=120) String nombre,
  @NotBlank @Size(max=80)  String marca,
  @NotBlank @Size(max=40)  String tipo,
  @NotNull  @DecimalMin("0.0") BigDecimal precio,
  @NotNull  Boolean disponible
) {

}