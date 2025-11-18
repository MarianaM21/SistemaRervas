package com.sistema_reservas.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class TestController {

    @GetMapping("/usuario")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> accesoUsuario() {
        return ResponseEntity.ok(Map.of(
                "mensaje", "Acceso permitido: ¡Eres USER!",
                "timestamp", System.currentTimeMillis()
        ));
    }
}
