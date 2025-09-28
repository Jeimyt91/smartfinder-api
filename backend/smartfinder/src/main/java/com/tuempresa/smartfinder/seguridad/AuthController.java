package com.tuempresa.smartfinder.seguridad;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth") // 👉 la URL base será: http://localhost:8081/auth/...
@CrossOrigin(origins = "*") // permite llamadas desde tu front (SPA o Postman)
public class AuthController {

    @Autowired
    private AuthService authService;

    // ✅ Registro de usuarios o administradores
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.status(201).body(authService.register(request));
    }

    // ✅ Login de usuario / admin
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
