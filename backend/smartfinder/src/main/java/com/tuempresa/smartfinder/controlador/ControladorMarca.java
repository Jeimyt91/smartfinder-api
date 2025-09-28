package com.tuempresa.smartfinder.controlador;

import com.tuempresa.smartfinder.dto.MarcaDTO;
import com.tuempresa.smartfinder.servicio.MarcaService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/brands")
public class ControladorMarca {

    private final MarcaService service;

    public ControladorMarca(MarcaService s){ this.service = s; }

    @GetMapping
    public List<MarcaDTO> listar() {            // <- ahora coincide con el service
        return service.listar();
    }
@GetMapping("/{id}")
public ResponseEntity<MarcaDTO> obtenerPorId(@PathVariable UUID id) {
    MarcaDTO marca = service.obtenerPorId(id);
    return ResponseEntity.ok(marca);
}
    @PostMapping
    public MarcaDTO crear(@RequestBody MarcaDTO dto) {
        return service.crear(dto);
    }
}
