package com.sistema_reservas.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sistema_reservas.controller.dto.PagoDTO;
import com.sistema_reservas.controller.dto.PagoResponseDTO;
import com.sistema_reservas.mapper.pagoMapper;
import com.sistema_reservas.model.Factura;
import com.sistema_reservas.model.Pago;
import com.sistema_reservas.model.Reserva;
import com.sistema_reservas.service.FacturaServiceimpl;
import com.sistema_reservas.service.PagoService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pagos")
public class PagoController {

    @Autowired
    private PagoService pagoService;

    @Autowired
    private pagoMapper pagoMapper;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private FacturaServiceimpl facturaService;

    // Crear pago
    @PostMapping
    public ResponseEntity<PagoResponseDTO> crearPago(@RequestBody PagoDTO dto) {
        Pago pago = pagoMapper.toEntity(dto);

        //reserva completa con usuario y espacio
        List<Reserva> reservas = entityManager.createQuery(
                        "SELECT r FROM Reserva r LEFT JOIN FETCH r.usuario LEFT JOIN FETCH r.espacio WHERE r.id_reserva = :id",
                        Reserva.class
                )
                .setParameter("id", dto.getReservaId())
                .getResultList();

        if (reservas.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new PagoResponseDTO("Reserva no encontrada para el pago"));
        }

        Reserva reservaCompleta = reservas.get(0);
        pago.setReserva(reservaCompleta);
        Pago pagoGuardado = pagoService.guardarPago(pago);
        PagoResponseDTO response = pagoMapper.toResponseDTO(pagoGuardado);
        response.setMensaje("Pago creado correctamente");

        return ResponseEntity.ok(response);
    }

    // Listar todos los pagos
    @GetMapping
    public ResponseEntity<List<PagoResponseDTO>> listarPagos() {
        List<Pago> pagos = pagoService.listarPagos();
        List<PagoResponseDTO> response = pagos.stream()
                .map(pagoMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    // Obtener pago por ID
    @GetMapping("/{id}")
    public ResponseEntity<PagoResponseDTO> obtenerPago(@PathVariable Long id) {
        Pago pago = pagoService.obtenerPagoPorId(id);
        if (pago == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new PagoResponseDTO("Pago no encontrado"));
        }
        PagoResponseDTO dto = pagoMapper.toResponseDTO(pago);
        return ResponseEntity.ok(dto);
    }

    //actualizar pago
    @PutMapping("/{id}")
    public ResponseEntity<PagoResponseDTO> actualizarPago(@PathVariable Long id, @RequestBody PagoDTO pagoDTO) {
        try {
            Pago pagoExistente = pagoService.obtenerPagoPorId(id);
            if (pagoExistente == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new PagoResponseDTO("Pago no encontrado para actualizar"));//404
            }

            //se actualizan los campos
            pagoExistente.setMonto(pagoDTO.getMonto());
            pagoExistente.setEstado(pagoDTO.getEstado());
            pagoExistente.setFechaPago(pagoDTO.getFechaPago());
            pagoExistente.setMetodo(pagoDTO.getMetodo());
            //reserva actualizada
            if (pagoDTO.getReservaId() != null) {
                List<Reserva> reservas = entityManager.createQuery(
                                "SELECT r FROM Reserva r LEFT JOIN FETCH r.usuario LEFT JOIN FETCH r.espacio WHERE r.id_reserva = :id",
                                Reserva.class
                        )
                        .setParameter("id", pagoDTO.getReservaId())
                        .getResultList();

                if (reservas.isEmpty()) {
                    return ResponseEntity.badRequest()
                            .body(new PagoResponseDTO("Reserva no encontrada para actualizar el pago"));
                }
                Reserva reservaCompleta = reservas.get(0);
                pagoExistente.setReserva(reservaCompleta);
            }

            Pago pagoActualizado = pagoService.guardarPago(pagoExistente);
            PagoResponseDTO responseDTO = pagoMapper.toResponseDTO(pagoActualizado);
            responseDTO.setMensaje("Pago actualizado correctamente");

            return ResponseEntity.ok(responseDTO);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new PagoResponseDTO("Error al actualizar el pago: " + e.getMessage()));
        }
    }


    // Eliminar pago
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarPago(@PathVariable Long id) {
        Pago pago = pagoService.obtenerPagoPorId(id);
        if (pago == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Pago no encontrado para eliminar");
        }
        pagoService.eliminarPago(id);
        return ResponseEntity.ok("Pago eliminado exitosamente");
    }


    //Recibir un evento de pago
    @PostMapping("/webhook")
    public ResponseEntity<String> recibirEventoPago(@RequestBody Pago pagoRecibido) {
        try {
            // Simulación de guardar o actualizar el pago en la BD
            Pago pagoExistente = pagoService.obtenerPagoPorId(pagoRecibido.getId());

            if (pagoExistente != null) {
                pagoExistente.setEstado(pagoRecibido.getEstado());
                pagoExistente.setMonto(pagoRecibido.getMonto());
                pagoService.actualizarPago(pagoExistente);
            } else {
                pagoService.guardarPago(pagoRecibido);
            }

            //factura automática
            Factura factura = new Factura();
            factura.setPago(pagoRecibido);
            factura.setDescripcion("Factura generada automáticamente desde webhook");
            factura.setTotal(pagoRecibido.getMonto());
            factura.setFechaGeneracion(LocalDateTime.now());
            facturaService.guardarFactura(factura);
            System.out.println("Factura generada automáticamente desde webhook: ID = " + factura.getId());

            return ResponseEntity.status(201).body("Evento de pago recibido correctamente");
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error procesando el evento de pago: " + e.getMessage());
        }
    }

     //descargar la factura asociada aun pago
     @GetMapping("/descargar/{id}")
     public ResponseEntity<?> descargarFactura(@PathVariable Long id) {
         try {
             Factura factura = facturaService.obtenerPorId(id);
             if (factura == null) {
                 return ResponseEntity.notFound().build();
             }
             //ObjectMapper LocalDateTime
             ObjectMapper objectMapper = new ObjectMapper();
             objectMapper.registerModule(new JavaTimeModule());
             objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);


             //formato json
             String jsonFactura = objectMapper
                     .writerWithDefaultPrettyPrinter()
                     .writeValueAsString(factura);
             // descargar
             HttpHeaders headers = new HttpHeaders();
             headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=factura_" + id + ".json");
             headers.add(HttpHeaders.CONTENT_TYPE, "application/json");

             return ResponseEntity.ok()
                     .headers(headers)
                     .body(jsonFactura);

         } catch (Exception e) {
             return ResponseEntity.internalServerError()
                     .body("Error al descargar la factura: " + e.getMessage());
         }
     }

}


