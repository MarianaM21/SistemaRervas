package com.sistema_reservas_copia.mapper;

import com.sistema_reservas_copia.controller.dto.ReservaDTO;
import com.sistema_reservas_copia.controller.dto.ReservaResponseDTO;
import com.sistema_reservas_copia.model.Reserva;
import com.sistema_reservas_copia.model.usuarios;
import com.sistema_reservas_copia.model.Espacio;

public class reservaMapper {

    public static ReservaResponseDTO toResponseDTO(Reserva reserva) {
        return new ReservaResponseDTO(
                reserva.getId(),
                reserva.getUsuario() != null ? reserva.getUsuario().getId() : null,
                reserva.getEspacio() != null ? reserva.getEspacio().getIdEspacio() : null,
                reserva.getFechaInicio(),
                reserva.getFechaFin(),
                reserva.getEstado(),
                ""
        );
    }

    public static Reserva toEntity(ReservaDTO dto, usuarios usuario, Espacio espacio) {
        if (dto == null) return null;
        Reserva reserva = new Reserva();
        reserva.setUsuario(usuario);
        reserva.setEspacio(espacio);
        reserva.setFechaInicio(dto.getFechaInicio());
        reserva.setFechaFin(dto.getFechaFin());
        reserva.setEstado(dto.getEstado());
        return reserva;
    }
}

