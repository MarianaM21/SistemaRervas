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

import java.util.List;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

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
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(reservaMapper.toResponseDTO(reserva));
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
                        .body(new ReservaResponseDTO("Usuario o espacio no existen"));
            }

            // se crea la reserva
            Reserva reserva = reservaMapper.toEntity(dto);
            reserva.setUsuario(usuario);
            reserva.setEspacio(espacio);

            Reserva nueva = reservaDAO.guardarReserva(reserva);

            // Respuesta
            ReservaResponseDTO responseDTO = reservaMapper.toResponseDTO(nueva);
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
                return ResponseEntity.notFound().build();
            }


            reservaExistente.setFecha(dto.getFecha());
            reservaExistente.setEstado(dto.getEstado());

            if (dto.getUsuarioId() != null) {
                Usuario usuario = usuarioDAO.buscarPorId(dto.getUsuarioId());
                reservaExistente.setUsuario(usuario);
            }
            if (dto.getEspacioId() != null) {
                Espacio espacio = espacioDAO.buscarPorId(dto.getEspacioId());
                reservaExistente.setEspacio(espacio);
            }

            Reserva actualizada = reservaDAO.guardarReserva(reservaExistente);
            return ResponseEntity.ok(reservaMapper.toResponseDTO(actualizada));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ReservaResponseDTO("Error al actualizar la reserva: " + e.getMessage()));
        }
    }

    // Eliminar reserva
    @DeleteMapping("eliminar/{id}")
    public ResponseEntity<String> eliminarReserva(@PathVariable Long id) {
        try {
            reservaServiceimpl.eliminarReserva(id);
            return ResponseEntity.ok("Reserva eliminada correctamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al eliminar la reserva: " + e.getMessage());
        }
    }
    //Listar reservas por estado y fecha
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
