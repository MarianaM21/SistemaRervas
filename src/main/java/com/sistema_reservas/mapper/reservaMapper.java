package com.sistema_reservas.mapper;

import com.sistema_reservas.controller.dto.ReservaDTO;
import com.sistema_reservas.controller.dto.ReservaResponseDTO;
import com.sistema_reservas.model.Reserva;
import org.springframework.stereotype.Component;

@Component
public class reservaMapper {

    // De DTO a entidad
    public Reserva toEntity(ReservaDTO dto) {
        Reserva reserva = new Reserva();
        reserva.setId_reserva(dto.getId_reserva());
        reserva.setFecha(dto.getFecha());
        reserva.setEstado(dto.getEstado());
        return reserva;
    }

    // De entidad a DTO
    public ReservaDTO toDTO(Reserva reserva) {
        ReservaDTO dto = new ReservaDTO();
        dto.setId_reserva(reserva.getId_reserva());
        dto.setUsuarioId(reserva.getUsuario() != null ? reserva.getUsuario().getId() : null);
        dto.setEspacioId(reserva.getEspacio() != null ? reserva.getEspacio().getIdEspacio() : null);
        dto.setFecha(reserva.getFecha());
        dto.setEstado(reserva.getEstado());
        return dto;
    }

    // De entidad a ResponseDTO
    public ReservaResponseDTO toResponseDTO(Reserva reserva) {
        return new ReservaResponseDTO(
                reserva.getId_reserva(),
                reserva.getUsuario() != null ? reserva.getUsuario().getId() : null,
                reserva.getUsuario() != null ? reserva.getUsuario().getNombre() : null,
                reserva.getEspacio() != null ? reserva.getEspacio().getIdEspacio() : null,
                reserva.getEspacio() != null ? reserva.getEspacio().getNombre() : null,
                reserva.getFecha(),
                reserva.getEstado()
        );
    }
}