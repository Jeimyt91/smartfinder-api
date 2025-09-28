package com.tuempresa.smartfinder.seguridad;

import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    // Simulación de base de datos en memoria (para ejemplo)
    private Map<String, String> usuarios = new HashMap<>();

    public Map<String, Object> register(RegisterRequest request) {
        usuarios.put(request.getEmailOrUsername(), request.getPassword());

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Admin creado");
        response.put("usuario", request.getEmailOrUsername());
        return response;
    }

    public Map<String, Object> login(LoginRequest request) {
        String storedPassword = usuarios.get(request.getEmailOrUsername());
        Map<String, Object> response = new HashMap<>();

        if (storedPassword != null && storedPassword.equals(request.getPassword())) {
            // ⚡ Aquí normalmente generarías un JWT real
            String fakeToken = "jwt-token-" + request.getEmailOrUsername();
            response.put("token", fakeToken);
            response.put("usuario", request.getEmailOrUsername());
        } else {
            response.put("error", "Credenciales inválidas");
        }

        return response;
    }
}
