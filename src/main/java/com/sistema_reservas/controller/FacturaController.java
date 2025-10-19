package com.sistema_reservas.controller;

import com.sistema_reservas.controller.dto.FacturaResponseDTO;
import com.sistema_reservas.mapper.facturaMapper;
import com.sistema_reservas.model.Factura;
import com.sistema_reservas.model.Pago;
import com.sistema_reservas.service.FacturaServiceimpl;
import com.sistema_reservas.service.PagoService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/facturas")
public class FacturaController {

    @Autowired
    private FacturaServiceimpl facturaService;

    @Autowired
    private PagoService pagoService;

    @Autowired
    private facturaMapper facturaMapper;

    @PersistenceContext
    private EntityManager entityManager;

    //Generar factura
    @PostMapping("/generar-manual")
    public ResponseEntity<FacturaResponseDTO> generarFactura(@RequestParam Long pagoId) {
        Pago pago = pagoService.obtenerPagoPorId(pagoId);
        if (pago == null) {
            FacturaResponseDTO error = new FacturaResponseDTO();
            error.setMensaje("Pago no encontrado para generar factura");
            return ResponseEntity.badRequest().body(error);
        }
        Factura existente = facturaService.obtenerPorPagoId(pagoId);
        if (existente != null) {
            FacturaResponseDTO error = new FacturaResponseDTO();
            error.setMensaje("Ya existe una factura para este pago");
            return ResponseEntity.badRequest().body(error);
        }

        Factura factura = new Factura();
        factura.setPago(pago);
        factura.setFechaGeneracion(LocalDateTime.now());
        factura.setTotal(pago.getMonto());
        factura.setDescripcion("Factura generada para el pago ID " + pagoId);

        Factura facturaGuardada = facturaService.guardarFactura(factura);
        FacturaResponseDTO response = facturaMapper.toDTO(facturaGuardada);
        response.setMensaje("Factura generada correctamente");

        return ResponseEntity.ok(response);
    }

    // Obtener factura por ID
    @GetMapping("/{id}")
    public ResponseEntity<FacturaResponseDTO> obtenerFactura(@PathVariable Long id) {
        Factura factura = facturaService.obtenerPorId(id);
        if (factura == null) {
            FacturaResponseDTO error = new FacturaResponseDTO();
            error.setMensaje("Factura no encontrada");
            return ResponseEntity.status(404).body(error);
        }

        FacturaResponseDTO dto = facturaMapper.toDTO(factura);
        dto.setMensaje("Factura encontrada correctamente");
        return ResponseEntity.ok(dto);

    }

    // Listar facturas
    @GetMapping
    public ResponseEntity<List<FacturaResponseDTO>> listarFacturas() {
        List<Factura> facturas = facturaService.listarFacturas();
        List<FacturaResponseDTO> response = facturas.stream()
                .map(facturaMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }


    // Eliminar factura
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarFactura(@PathVariable Long id) {
        Factura factura = facturaService.obtenerPorId(id);
        if (factura == null) {
            return ResponseEntity.status(404).body("Factura no encontrada");
        }
        facturaService.eliminarFactura(id);
        return ResponseEntity.ok("Factura eliminada exitosamente");
    }

}
