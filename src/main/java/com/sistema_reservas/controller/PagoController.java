package com.sistema_reservas.controller;

import com.sistema_reservas.controller.dto.PagoDTO;
import com.sistema_reservas.controller.dto.PagoResponseDTO;
import com.sistema_reservas.model.Pago;
import com.sistema_reservas.service.PagoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/pagos")
public class PagoController {

    private final PagoService pagoService;

    public PagoController(PagoService pagoService) {
        this.pagoService = pagoService;
    }

    @GetMapping
    public ResponseEntity<List<Pago>> listarPagos() {
        return ResponseEntity.ok(pagoService.listarPagos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pago> buscarPago(@PathVariable Long id) {
        return pagoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PagoResponseDTO> registrarPago(@RequestBody PagoDTO dto) {
        return ResponseEntity.ok(pagoService.registrarPago(dto));
    }

    @PutMapping("/{id}/confirmar")
    public ResponseEntity<PagoResponseDTO> confirmarPago(@PathVariable Long id) {
        return ResponseEntity.ok(pagoService.confirmarPago(id));
    }

    @PutMapping("/{id}/fallido")
    public ResponseEntity<PagoResponseDTO> marcarPagoFallido(@PathVariable Long id) {
        return ResponseEntity.ok(pagoService.marcarPagoFallido(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPago(@PathVariable Long id) {
        pagoService.eliminarPago(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/reserva/{reservaId}/pendientes")
    public ResponseEntity<List<Pago>> pagosPendientesPorReserva(@PathVariable Long reservaId) {
        return ResponseEntity.ok(pagoService.listarPagosPendientesPorReserva(reservaId));
    }

    @GetMapping("/reserva/{reservaId}/confirmados")
    public ResponseEntity<List<Pago>> pagosConfirmadosPorReserva(@PathVariable Long reservaId) {
        return ResponseEntity.ok(pagoService.listarPagosConfirmadosPorReserva(reservaId));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Pago>> pagosPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(pagoService.listarPagosPorUsuario(usuarioId));
    }
}
