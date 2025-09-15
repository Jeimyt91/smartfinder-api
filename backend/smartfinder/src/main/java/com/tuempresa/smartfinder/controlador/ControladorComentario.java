package com.tuempresa.smartfinder.controlador;


import com.tuempresa.smartfinder.dto.*; import com.tuempresa.smartfinder.servicio.*;
import org.springframework.web.bind.annotation.*;
import java.util.*; import java.util.UUID;


@RestController @RequestMapping("/api/comments")
public class ControladorComentario {
private final ComentarioService service;
public ControladorComentario(ComentarioService s){ this.service=s; }


@GetMapping("/device/{deviceId}")
public List<ComentarioDTO> porDispositivo(@PathVariable UUID deviceId){ return service.listar(deviceId); }


@PostMapping("/device/{deviceId}")
public ComentarioDTO crear(@PathVariable UUID deviceId, @RequestBody Map<String,Object> body){
String author = (String) body.getOrDefault("author","Anónimo");
Integer rating = body.get("rating")==null?5:((Number)body.get("rating")).intValue();
String text = (String) body.getOrDefault("text"," ");
return service.crear(deviceId, author, rating, text);
}
}