package com.tuempresa.smartfinder.configuracion;


import org.springframework.http.*; import org.springframework.web.bind.MethodArgumentNotValidException; import org.springframework.web.bind.annotation.*;
import java.util.*;


@RestControllerAdvice
public class ManejadorExcepcionesGlobal {
@ExceptionHandler(NoSuchElementException.class)
public ResponseEntity<?> notFound(NoSuchElementException ex){ return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error","No encontrado")); }
@ExceptionHandler(MethodArgumentNotValidException.class)
public ResponseEntity<?> badReq(MethodArgumentNotValidException ex){ return ResponseEntity.badRequest().body(Map.of("error","Datos inválidos")); }
}