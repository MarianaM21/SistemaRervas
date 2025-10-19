package com.sistema_reservas.controller;

import com.sistema_reservas.controller.dto.EspacioDTO;
import com.sistema_reservas.controller.dto.EspacioResponseDTO;
import com.sistema_reservas.dao.espacioDAO;
import com.sistema_reservas.model.Espacio;
import com.sistema_reservas.service.EspacioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestController
@RequestMapping("/api/espacios")
public class EspacioController {
    private static final Logger logger = LoggerFactory.getLogger(EspacioController.class);
    private final EspacioService espacioService;

    public EspacioController(EspacioService espacioService) {
        this.espacioService = espacioService;
    }

    // Manejo de errores de validación al estilo que querías
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationError(MethodArgumentNotValidException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Tienes datos inválidos");

        logger.warn("Error de validación: {}", ex.getBindingResult().getFieldErrors());
        return ResponseEntity.badRequest().body(response);
    }


    @PostMapping("/guardar")
    public ResponseEntity<EspacioResponseDTO> guardarEspacio(@Valid @RequestBody EspacioDTO dto) {
        return ResponseEntity.ok(espacioService.guardarEspacio(dto));
    }


    @GetMapping("/listar")
    public ResponseEntity<List<EspacioResponseDTO>> listarEspacios() {
        return ResponseEntity.ok(espacioService.listarEspacios());
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<?> actualizarEspacio(@PathVariable Long id, @RequestBody EspacioDTO dto) {
        try {
            EspacioResponseDTO response = espacioService.actualizarEspacio(id, dto);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage()); // 404
        }
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminarEspacio(@PathVariable Long id) {
        try {
            espacioService.eliminarEspacio(id);
            return ResponseEntity.ok("Espacio eliminado exitosamente"); // 200
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); // 404
        }
    }


    //buscar por id
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        try {
            EspacioResponseDTO response = espacioService.buscarPorId(id);
            return ResponseEntity.ok(response); // 200
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); // 404
        }
    }
}
