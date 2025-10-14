package com.sistema_reservas.controller;

import com.sistema_reservas.controller.dto.EspacioDTO;
import com.sistema_reservas.controller.dto.EspacioResponseDTO;
import com.sistema_reservas.service.EspacioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/espacios")
public class EspacioController {

    private final EspacioService espacioService;

    public EspacioController(EspacioService espacioService) {
        this.espacioService = espacioService;
    }

    @PostMapping("/guardar")
    public ResponseEntity<EspacioResponseDTO> guardarEspacio(@RequestBody EspacioDTO dto) {
        return ResponseEntity.ok(espacioService.guardarEspacio(dto));
    }

    @GetMapping("/listar")
    public ResponseEntity<List<EspacioResponseDTO>> listarEspacios() {
        return ResponseEntity.ok(espacioService.listarEspacios());
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<EspacioResponseDTO> actualizarEspacio(@PathVariable Long id, @RequestBody EspacioDTO dto) {
        return ResponseEntity.ok(espacioService.actualizarEspacio(id, dto));
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminarEspacio(@PathVariable Long id) {
        espacioService.eliminarEspacio(id);
        return ResponseEntity.ok("Espacio eliminado exitosamente");
    }
//buscar por id
    @GetMapping("/{id}")
    public ResponseEntity<EspacioResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(espacioService.buscarPorId(id));
    }
}
