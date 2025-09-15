package com.tuempresa.smartfinder.controlador;


import com.tuempresa.smartfinder.dto.*; import com.tuempresa.smartfinder.servicio.*;
import org.springframework.web.bind.annotation.*;
import java.util.*; import java.util.UUID;


@RestController @RequestMapping("/api/devices")
public class ControladorDispositivo {
private final DispositivoService service;
public ControladorDispositivo(DispositivoService s){ this.service=s; }


@GetMapping
public List<DispositivoDTO> listar(@RequestParam(required=false) String q,
@RequestParam(required=false) UUID brandId,
@RequestParam(required=false) String type,
@RequestParam(defaultValue="release_desc") String sort){
return service.listar(q, brandId, type, sort);
}


@GetMapping("/{id}") public DispositivoDTO uno(@PathVariable UUID id){ return service.obtener(id); }
@PostMapping public DispositivoDTO crear(@RequestBody DispositivoCrearActualizarDTO in){ return service.crear(in); }
@PutMapping("/{id}") public DispositivoDTO actualizar(@PathVariable UUID id, @RequestBody DispositivoCrearActualizarDTO in){ return service.actualizar(id, in); }
@DeleteMapping("/{id}") public void eliminar(@PathVariable UUID id){ service.eliminar(id); }
}