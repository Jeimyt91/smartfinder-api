package com.tuempresa.smartfinder.controlador;

import com.tuempresa.smartfinder.dto.ComentarioDTO;
import com.tuempresa.smartfinder.servicio.ComentarioService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dispositivos/{deviceId}/comentarios")
public class ControladorComentario {

  private final ComentarioService servicio;

  public ControladorComentario(ComentarioService servicio) {
    this.servicio = servicio;
  }

  @GetMapping
  public List<ComentarioDTO> listar(@PathVariable Long deviceId) {
    return servicio.listar(deviceId);
  }

  @PostMapping
  public ComentarioDTO crear(@PathVariable Long deviceId,
                             @RequestParam(required = false) String author,
                             @RequestParam Integer rating,
                             @RequestParam String text) {
    return servicio.crear(deviceId, author, rating, text);
  }
}
