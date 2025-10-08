package com.sistema_reservas.controller;

import com.sistema_reservas.controller.dto.*;
import com.sistema_reservas.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioService usuarioService;

    @PostMapping("/register")
    public ResponseEntity<UserResponseRegisterDTO> register(@RequestBody UserRegisterDTO dto) {
        return ResponseEntity.ok(usuarioService.registerUser(dto));
    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody UserLoginDTO dto) {
        LoginResponseDTO response = usuarioService.login(dto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/recuperar-contraseña")
    public ResponseEntity<?> forgotPassword(@RequestBody OlvideContraseñaDTO request) {
        try {
            usuarioService.OlvideContraseña(request);
            return ResponseEntity.ok("Correo de recuperación enviado correctamente (simulado)");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/restaurar-contraseña")
    public ResponseEntity<?> resetPassword(@RequestBody RestaurarContraseñaDTO request) {
        try {
            usuarioService.RestaurarContraseña(request);
            return ResponseEntity.ok("Contraseña restablecida correctamente");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al restablecer la contraseña");
        }
    }
}
