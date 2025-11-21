package com.sistema_reservas.controller;

import com.sistema_reservas.controller.dto.CambioPasswordDTO;
import com.sistema_reservas.controller.dto.usuarioDTO;
import com.sistema_reservas.controller.dto.UsuarioResponseDTO;
import com.sistema_reservas.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios")

public class UsuarioController {
    private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);


    @Autowired
    private UsuarioService usuarioService;

    // Listar todos los usuarios
    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listarUsuarios() {
        List<UsuarioResponseDTO> usuarios = usuarioService.listarUsuarios();
        if (usuarios.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());//404
        }
        return ResponseEntity.ok(usuarios);
    }


    // Obtener usuario por ID
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> obtenerUsuarioPorId(@PathVariable Long id) {
        UsuarioResponseDTO usuario = usuarioService.obtenerUsuarioPorId(id);
        if (usuario == null) {
            logger.warn("Usuario con ID {} no encontrado", id);
            return ResponseEntity.notFound().build();  //404
        }
        return ResponseEntity.ok(usuario);
    }

    // Actualizar usuario por ID
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> actualizarUsuario(@PathVariable Long id,
                                                                @RequestBody usuarioDTO usuarioDTO) {
        UsuarioResponseDTO actualizado = usuarioService.actualizarUsuario(id, usuarioDTO);
        if (actualizado == null) {
            logger.warn("Usuario con ID {} no encontrado para actualizar", id);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarUsuario(@PathVariable Long id) {
        boolean eliminado = usuarioService.eliminarUsuario(id);
        if (!eliminado) {
            return ResponseEntity.status(404).body("Usuario no encontrado ");
        }
        return ResponseEntity.ok("Se eliminó correctamente el usuario");
    }

    // Obtener perfil del usuario autenticado
    @GetMapping("/me")
    public ResponseEntity<UsuarioResponseDTO> obtenerMiPerfil() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(usuarioService.obtenerPorEmail(email));
    }

    // Actualizar perfil del usuario autenticado
    @PutMapping("/me")
    public ResponseEntity<UsuarioResponseDTO> actualizarMiPerfil(@RequestBody usuarioDTO dto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(usuarioService.actualizarMiUsuario(email, dto));
    }

    // Cambiar contraseña del usuario autenticado
    @PutMapping("/me/password")
    public ResponseEntity<String> cambiarPassword(@RequestBody CambioPasswordDTO dto) {
        usuarioService.cambiarMiPassword(dto);
        return ResponseEntity.ok("Contraseña actualizada correctamente");
    }

    // Cerrar sesiones activas
    @PostMapping("/me/cerrar-sesiones")
    public ResponseEntity<String> cerrarSesiones() {
        usuarioService.cerrarSesionesActuales();
        return ResponseEntity.ok("Sesiones cerradas correctamente");
    }

}

