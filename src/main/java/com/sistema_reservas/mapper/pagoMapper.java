package com.sistema_reservas.mapper;

import com.sistema_reservas.controller.dto.PagoDTO;
import com.sistema_reservas.controller.dto.PagoResponseDTO;
import com.sistema_reservas.model.Pago;
import com.sistema_reservas.model.Reserva;
import org.springframework.stereotype.Component;

@Component
public class pagoMapper {

    // el DTO se convierte al lenguaje entidad
    public Pago toEntity(PagoDTO dto) {
        Pago pago = new Pago();
        pago.setMonto(dto.getMonto());
        pago.setEstado(dto.getEstado());
        pago.setFechaPago(dto.getFechaPago());
        pago.setMetodo(dto.getMetodo());


        if (dto.getReservaId() != null) {
            Reserva reserva = new Reserva();
            reserva.setId_reserva(dto.getReservaId());
            pago.setReserva(reserva);
        }
        return pago;
    }

    // de lenguaje entidad a DTO de respuesta
    public PagoResponseDTO toResponseDTO(Pago pago) {
        PagoResponseDTO dto = new PagoResponseDTO();
        dto.setId(pago.getId());
        dto.setMonto(pago.getMonto());
        dto.setEstado(pago.getEstado());
        dto.setFechaPago(pago.getFechaPago());
        dto.setMetodo(pago.getMetodo());

        if (pago.getReserva() != null) {
            dto.setReservaId(pago.getReserva().getId_reserva());


            if (pago.getReserva().getUsuario() != null) {
                dto.setNombreUsuario(pago.getReserva().getUsuario().getNombre());
            } else {
                dto.setNombreUsuario("Usuario no asignado");
            }


            if (pago.getReserva().getEspacio() != null) {
                dto.setNombreEspacio(pago.getReserva().getEspacio().getNombre());
            } else {
                dto.setNombreEspacio("Espacio no asignado");
            }
        }
        return dto;
    }
}
