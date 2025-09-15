package com.tuempresa.smartfinder.controlador;


import com.tuempresa.smartfinder.dto.*; import com.tuempresa.smartfinder.servicio.*;
import org.springframework.web.bind.annotation.*;
import java.util.*; import java.util.UUID;


@RestController @RequestMapping("/api/brands")
public class ControladorMarca {
private final MarcaService service;
public ControladorMarca(MarcaService s){ this.service=s; }
@GetMapping public List<MarcaDTO> listar(){ return service.listar(); }
@PostMapping public MarcaDTO crear(@RequestBody Map<String,String> body){ return service.crear(body.getOrDefault("name","")); }
}