package com.sistema_reservas.controller;

import com.sistema_reservas.controller.dto.*;
import com.sistema_reservas.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioService usuarioService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserRegisterDTO dto) {
        try {
            return ResponseEntity.ok(usuarioService.registerUser(dto));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationError(MethodArgumentNotValidException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "tienes datos invalidos");
        return ResponseEntity.badRequest().body(response);
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserLoginDTO dto) {
        try {
            LoginResponseDTO response = usuarioService.login(dto);
            return ResponseEntity.ok(response); // 200 OK
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());// Credenciales inválidas
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); // 404Usuario no encontrado
        }
    }

    @PostMapping("/recuperar-contraseña")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody OlvideContraseñaDTO request) {
        try {
            usuarioService.OlvideContraseña(request);
            return ResponseEntity.ok("Correo de recuperación enviado correctamente (simulado)");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage()); // 400datos inválidos
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());  // 404usuario no encontrado
        }
    }


    @PostMapping("/restaurar-contraseña")
    public ResponseEntity<?> resetPassword(@RequestBody RestaurarContraseñaDTO request) {
        try {
            usuarioService.RestaurarContraseña(request);
            return ResponseEntity.ok("Contraseña restablecida correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); // 404 si usuario no existe
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al restablecer la contraseña"); // 500
        }
    }

}