package com.sistema_reservas.controller;

import com.sistema_reservas.controller.dto.ReservaDTO;
import com.sistema_reservas.controller.dto.ReservaResponseDTO;
import com.sistema_reservas.dao.UsuarioDAO;
import com.sistema_reservas.dao.espacioDAO;
import com.sistema_reservas.model.Reserva;
import com.sistema_reservas.model.Usuario;
import com.sistema_reservas.model.Espacio;
import com.sistema_reservas.service.ReservaServiceimpl;
import com.sistema_reservas.mapper.reservaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    private static final Logger logger = LoggerFactory.getLogger(ReservaController.class);

    @Autowired
    private ReservaServiceimpl reservaServiceimpl;

    @Autowired
    private UsuarioDAO usuarioDAO;

    @Autowired
    private espacioDAO espacioDAO;

    @Autowired
    private reservaMapper reservaMapper;

    @Autowired
    private com.sistema_reservas.security.ReservaSecurity reservaSecurity;

    // Listar todas las reservas
    @GetMapping("/listar")
    @PreAuthorize("hasRole('ADMIN')")
    public List<ReservaResponseDTO> listarReservas() {
        List<Reserva> reservas = reservaServiceimpl.listarReservas();
        return reservas.stream()
                .map(reservaMapper::toResponseDTO)
                .toList();
    }

    // Obtener una reserva por ID
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @reservaSecurity.esPropietario(#id, authentication.name)")
    public ResponseEntity<ReservaResponseDTO> obtenerReservaPorId(@PathVariable Long id) {
        Reserva reserva = reservaServiceimpl.obtenerReservaPorId(id);
        if (reserva == null) {
            logger.warn("Reserva con ID {} no encontrada", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(reservaMapper.toResponseDTO(reserva));
    }

    // Mis reservas
    @GetMapping("/mis-reservas")
    @PreAuthorize("hasAnyRole('USER','AFILIADO')")
    public ResponseEntity<List<ReservaResponseDTO>> listarMisReservas(Authentication auth) {
        try {
            String email = auth.getName(); // email del usuario autenticado

            List<Reserva> reservas = reservaServiceimpl.listarReservasPorEmailUsuario(email);

            List<ReservaResponseDTO> response = reservas.stream()
                    .map(reservaMapper::toResponseDTO)
                    .toList();

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    // Crear reserva
    @PostMapping
    @PreAuthorize("hasAnyRole('USER','AFILIADO','ADMIN')")
    public ResponseEntity<ReservaResponseDTO> guardarReserva(@RequestBody ReservaDTO dto, Authentication auth) {
        try {
            Usuario usuario = usuarioDAO.buscarPorId(dto.getUsuarioId());
            Espacio espacio = espacioDAO.buscarPorId(dto.getEspacioId());

            if (usuario == null || espacio == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ReservaResponseDTO("Error: Comprueba el Id de espacio o usuario"));
            }

            boolean esAdmin = auth.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

            if (!esAdmin && !usuario.getEmail().equals(auth.getName())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ReservaResponseDTO("No puedes crear reserva para otro usuario"));
            }
            String estadoEspacio = espacio.getEstado() != null
                    ? espacio.getEstado().trim().toLowerCase()
                    : "";

            if (estadoEspacio.contains("ocup") || estadoEspacio.contains("reserv")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ReservaResponseDTO("El recurso no está disponible para reservar."));
            }

            // Crear la reserva desde el DTO
            Reserva reserva = reservaMapper.toEntity(dto);
            reserva.setUsuario(usuario);
            reserva.setEspacio(espacio);

            if (reserva.getEstado() == null || reserva.getEstado().isBlank()) {
                reserva.setEstado("PENDIENTE");
            }

            espacio.setEstado("RESERVADO");
            espacioDAO.guardar(espacio);

            // Guardar la reserva
            Reserva nueva = reservaServiceimpl.guardarReserva(reserva);
            ReservaResponseDTO responseDTO = reservaMapper.toResponseDTO(nueva);
            responseDTO.setMensaje("Reserva creada exitosamente");

            return ResponseEntity.ok(responseDTO);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ReservaResponseDTO("Error al guardar la reserva: " + e.getMessage()));
        }
    }

    // Actualizar reserva
    @PutMapping("/actualizar/{id}")
    @PreAuthorize("hasRole('ADMIN') or @reservaSecurity.esPropietario(#id, authentication.name)")
    public ResponseEntity<ReservaResponseDTO> actualizarReserva(@PathVariable Long id,
                                                                @RequestBody ReservaDTO dto,
                                                                Authentication auth) {
        try {
            Reserva reservaExistente = reservaServiceimpl.obtenerReservaPorId(id);
            if (reservaExistente == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ReservaResponseDTO("Reserva no encontrada"));
            }
            if (dto.getFecha() != null) {
                reservaExistente.setFecha(dto.getFecha());
            }

            if (dto.getEstado() != null) {
                reservaExistente.setEstado(dto.getEstado());
                if (dto.getEstado().equalsIgnoreCase("CANCELADA")
                        || dto.getEstado().equalsIgnoreCase("FINALIZADA")) {

                    Espacio espacioAsociado = reservaExistente.getEspacio();
                    if (espacioAsociado != null) {
                        espacioAsociado.setEstado("DISPONIBLE");
                    }
                }
            }
            if (dto.getUsuarioId() != null) {
                Usuario usuario = usuarioDAO.buscarPorId(dto.getUsuarioId());
                if (usuario == null) {
                    return ResponseEntity.badRequest()
                            .body(new ReservaResponseDTO("Usuario no encontrado"));
                }
                reservaExistente.setUsuario(usuario);
            }

            // Espacio
            if (dto.getEspacioId() != null) {
                Espacio espacio = espacioDAO.buscarPorId(dto.getEspacioId());
                if (espacio == null) {
                    return ResponseEntity.badRequest()
                            .body(new ReservaResponseDTO("Espacio no encontrado"));
                }
                reservaExistente.setEspacio(espacio);
            }

            Reserva actualizada = reservaServiceimpl.guardarReserva(reservaExistente);
            ReservaResponseDTO response = reservaMapper.toResponseDTO(actualizada);
            response.setMensaje("Reserva actualizada exitosamente");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ReservaResponseDTO("Error al actualizar la reserva: " + e.getMessage()));
        }
    }

    // Filtrar reservas
    @GetMapping("/filtrar")
    @PreAuthorize("hasRole('ADMIN')")
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
    @DeleteMapping("/eliminar/{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('AFILIADO') && @reservaSecurity.esPropietario(#id, authentication.name))")
    public ResponseEntity<?> eliminarReserva(@PathVariable Long id) {
        try {
            Reserva reserva = reservaServiceimpl.obtenerReservaPorId(id);

            if (reserva == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Reserva no encontrada"));
            }

            // Liberar el espacio automáticamente al cancelar
            Espacio espacio = reserva.getEspacio();
            if (espacio != null) {
                espacio.setEstado("DISPONIBLE");
                espacioDAO.guardar(espacio); //
            }

            boolean eliminado = reservaServiceimpl.eliminarReserva(id);

            if (eliminado) {
                return ResponseEntity.ok(Map.of("mensaje", "Reserva eliminada"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Reserva no encontrada"));
            }

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

}
