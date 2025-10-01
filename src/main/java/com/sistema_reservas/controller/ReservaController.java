package com.sistema_reservas.controller;

import com.sistema_reservas.controller.dto.ReservaDTO;
import com.sistema_reservas.controller.dto.ReservaResponseDTO;
import com.sistema_reservas.service.ReservaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    // Crear reserva
    @PostMapping
    public ResponseEntity<ReservaResponseDTO> crearReserva(@RequestBody ReservaDTO dto) {
        ReservaResponseDTO response = reservaService.crearReserva(dto);
        return ResponseEntity.ok(response);
    }

    // Actualizar estado de la reserva (ejemplo: CANCELAR)
    @PutMapping("/{id}/estado")
    public ResponseEntity<ReservaResponseDTO> actualizarEstado(
            @PathVariable Long id,
            @RequestParam String estado) {
        ReservaResponseDTO response = reservaService.actualizarEstado(id, estado);
        return ResponseEntity.ok(response);
    }

    // Eliminar reserva
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarReserva(@PathVariable Long id) {
        String mensaje = reservaService.eliminarReserva(id);
        return ResponseEntity.ok(mensaje);
    }
}
