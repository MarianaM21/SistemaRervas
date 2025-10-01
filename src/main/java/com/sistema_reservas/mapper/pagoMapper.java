package com.sistema_reservas.mapper;

import com.sistema_reservas.controller.dto.PagoDTO;
import com.sistema_reservas.controller.dto.PagoResponseDTO;
import com.sistema_reservas.model.Pago;
import jakarta.validation.constraints.Null;

public class pagoMapper {

    public static PagoResponseDTO toResponseDTO(Pago pago, String mensaje) {
        return new PagoResponseDTO(
                pago.getId(),
                pago.getReserva() != null ? pago.getReserva().getId() : null,
                pago.getMonto(),
                null,
                pago.getEstado(),
                pago.getFechaPago(),
                mensaje
        );
    }

    public static Pago toEntity(PagoDTO dto, Pago pago) {
        if (dto == null) return null;
        pago.setMonto(dto.getMonto());
        pago.setMetodo(dto.getMetodo());
        return pago;
    }
}
