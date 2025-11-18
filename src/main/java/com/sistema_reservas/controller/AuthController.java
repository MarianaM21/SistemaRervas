package com.sistema_reservas.controller;

import com.sistema_reservas.controller.dto.*;
import com.sistema_reservas.model.RefreshToken;
import com.sistema_reservas.security.JwtUtil;
import com.sistema_reservas.service.RefreshTokenService;
import com.sistema_reservas.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioService usuarioService;
    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;


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
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserLoginDTO dto) {
        try {
            LoginResponseDTO response = usuarioService.login(dto);
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("INVALID_PASSWORD");

        } catch (RuntimeException e) {
            if ("EMAIL_NOT_FOUND".equals(e.getMessage())) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("EMAIL_NOT_FOUND");
            }
            return ResponseEntity.badRequest().body("LOGIN_ERROR");
        }
    }



    @PostMapping("/recuperar-contraseña")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody OlvideContraseñaDTO request) {
        try {
            usuarioService.OlvideContraseña(request);
            return ResponseEntity.ok(Collections.singletonMap("message", "Correo de recuperación enviado correctamente"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error", e.getMessage()));
        }
    }


    @PostMapping("/restaurar-contraseña")
    public ResponseEntity<?> resetPassword(@RequestBody RestaurarContraseñaDTO request) {
        try {
            usuarioService.RestaurarContraseña(request);
            return ResponseEntity.ok(Collections.singletonMap("message", "Contraseña restablecida correctamente"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Collections.singletonMap("error", "Error al restablecer la contraseña"));
        }
    }


    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody RefreshRequestDTO request) {
        try {
            String refreshToken = request.getRefreshToken();
            if (refreshToken == null || refreshToken.isBlank()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Refresh token es obligatorio"));
            }

            refreshTokenService.revocarRefreshToken(refreshToken);
            return ResponseEntity.ok(Map.of("mensaje", "Logout exitoso"));

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}