package com.sistema_reservas.controller;

import com.sistema_reservas.controller.dto.ReservaDTO;
import com.sistema_reservas.controller.dto.ReservaResponseDTO;
import com.sistema_reservas.dao.reservaDAO;
import com.sistema_reservas.dao.usuarioDAO;
import com.sistema_reservas.dao.espacioDAO;
import com.sistema_reservas.model.Reserva;
import com.sistema_reservas.model.Usuario;
import com.sistema_reservas.model.Espacio;
import com.sistema_reservas.service.ReservaServiceimpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {
    private static final Logger logger = LoggerFactory.getLogger(ReservaController.class);

    @Autowired
    private ReservaServiceimpl reservaServiceimpl;

    @Autowired
    private reservaDAO reservaDAO;

    @Autowired
    private usuarioDAO usuarioDAO;

    @Autowired
    private espacioDAO espacioDAO;

    @Autowired
    private com.sistema_reservas.mapper.reservaMapper reservaMapper;

    // Listar reservas
    @GetMapping("/listar")
    public List<ReservaResponseDTO> listarReservas() {
        List<Reserva> reservas = reservaServiceimpl.listarReservas();
        return reservas.stream()
                .map(reservaMapper::toResponseDTO)
                .toList();
    }

    // listar una reserva por Id
    @GetMapping("/{id}")
    public ResponseEntity<ReservaResponseDTO> obtenerReservaPorId(@PathVariable Long id) {
        Reserva reserva = reservaServiceimpl.obtenerReservaPorId(id);
        if (reserva == null) {
            logger.warn("Reserva con ID {} no encontrada", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)//404
                    .body(null);
        }
        return ResponseEntity.ok(reservaMapper.toResponseDTO(reserva));//200
    }

    // Guardar una reserva
    @PostMapping
    public ResponseEntity<ReservaResponseDTO> guardarReserva(@RequestBody ReservaDTO dto) {
        try {
            // Buscar usuario y espacio existentes
            Usuario usuario = usuarioDAO.buscarPorId(dto.getUsuarioId());
            Espacio espacio = espacioDAO.buscarPorId(dto.getEspacioId());

            if (usuario == null || espacio == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ReservaResponseDTO("Error: Comprueba el Id de espacio o usuario"));
            }

            Reserva reserva = reservaMapper.toEntity(dto);
            reserva.setUsuario(usuario);
            reserva.setEspacio(espacio);

            Reserva nueva = reservaDAO.guardarReserva(reserva);
            ReservaResponseDTO responseDTO = reservaMapper.toResponseDTO(nueva);
            responseDTO.setMensaje("Reserva creada exitosamente");
            return ResponseEntity.ok(responseDTO);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ReservaResponseDTO("Error al guardar la reserva: " + e.getMessage()));
        }
    }


    // Actualizar reserva
    @PutMapping("actualizar/{id}")
    public ResponseEntity<ReservaResponseDTO> actualizarReserva(@PathVariable Long id, @RequestBody ReservaDTO dto) {
        try {
            Reserva reservaExistente = reservaServiceimpl.obtenerReservaPorId(id);
            if (reservaExistente == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ReservaResponseDTO("Reserva no encontrada"));
            }


            reservaExistente.setFecha(dto.getFecha());
            reservaExistente.setEstado(dto.getEstado());

            if (dto.getUsuarioId() != null) {
                Usuario usuario = usuarioDAO.buscarPorId(dto.getUsuarioId());
                if (usuario == null) {
                    return ResponseEntity.badRequest()
                            .body(new ReservaResponseDTO("Usuario no encontrado"));
                }
                reservaExistente.setUsuario(usuario);
            }
            if (dto.getEspacioId() != null) {
                Espacio espacio = espacioDAO.buscarPorId(dto.getEspacioId());
                if (espacio == null) {
                    return ResponseEntity.badRequest()
                            .body(new ReservaResponseDTO("Espacio no encontrado"));
                }
                reservaExistente.setEspacio(espacio);
            }


            Reserva actualizada = reservaDAO.guardarReserva(reservaExistente);

            ReservaResponseDTO response = reservaMapper.toResponseDTO(actualizada);
            response.setMensaje("Reserva actualizada exitosamente");
            return ResponseEntity.ok(response);


        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ReservaResponseDTO("Error al actualizar la reserva: " + e.getMessage()));
        }
    }

    // Eliminar reserva
    @GetMapping("/filtrar")
    public ResponseEntity<List<ReservaResponseDTO>> listarPorEstadoYFecha(
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) String fecha) {
        try {
            List<Reserva> reservas = reservaServiceimpl.listarReservasPorEstadoYFecha(estado, fecha);
            List<ReservaResponseDTO> response = reservas.stream()
                    .map(reservaMapper::toResponseDTO)
                    .toList();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
