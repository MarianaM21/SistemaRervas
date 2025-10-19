package com.sistema_reservas.controller;

import com.sistema_reservas.controller.dto.usuarioDTO;
import com.sistema_reservas.controller.dto.UsuarioResponseDTO;
import com.sistema_reservas.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
}

