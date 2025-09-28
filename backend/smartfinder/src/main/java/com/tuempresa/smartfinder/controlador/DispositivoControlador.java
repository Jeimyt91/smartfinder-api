// controlador/DispositivoControlador.java
package com.tuempresa.smartfinder.controlador;
import com.tuempresa.smartfinder.dominio.Dispositivo;
import com.tuempresa.smartfinder.dto.DispositivoDTO;
import com.tuempresa.smartfinder.servicio.DispositivoServicio;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dispositivos")
public class DispositivoControlador {
  private final DispositivoServicio servicio;
  public DispositivoControlador(DispositivoServicio servicio){ this.servicio = servicio; }
  // GET lista paginada
  @GetMapping
  public Page<Dispositivo> listar(@RequestParam(defaultValue="0") int page,
                                  @RequestParam(defaultValue="10") int size){
    return servicio.listar(page, size);
  }
  // GET por id
  @GetMapping("/{id}")
  public Dispositivo obtener(@PathVariable Long id){ return servicio.obtener(id); }
  // POST crear
  @PostMapping
  public ResponseEntity<Dispositivo> crear(@Valid @RequestBody DispositivoDTO dto){
    return ResponseEntity.status(HttpStatus.CREATED).body(servicio.crear(dto));
  }
  // PUT actualizar
  @PutMapping("/{id}")
  public Dispositivo actualizar(@PathVariable Long id, @Valid @RequestBody DispositivoDTO dto){
    return servicio.actualizar(id, dto);
  }
  // DELETE eliminar
  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void eliminar(@PathVariable Long id){ servicio.eliminar(id); }
}
