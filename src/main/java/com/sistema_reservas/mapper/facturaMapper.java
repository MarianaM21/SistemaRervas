package com.sistema_reservas.mapper;

import com.sistema_reservas.controller.dto.FacturaResponseDTO;
import com.sistema_reservas.model.Factura;
import org.springframework.stereotype.Component;

@Component
public class facturaMapper {

    public FacturaResponseDTO toDTO(Factura factura) {
        FacturaResponseDTO dto = new FacturaResponseDTO();
        dto.setId(factura.getId());
        dto.setPagoId(factura.getPago().getId());
        dto.setTotal(factura.getTotal());
        dto.setDescripcion(factura.getDescripcion());
        dto.setFechaGeneracion(factura.getFechaGeneracion());
        return dto;
    }
}
