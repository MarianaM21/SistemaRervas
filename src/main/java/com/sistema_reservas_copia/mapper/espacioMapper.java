package com.sistema_reservas_copia.mapper;

import com.sistema_reservas_copia.controller.dto.EspacioDTO;
import com.sistema_reservas_copia.controller.dto.EspacioResponseDTO;
import com.sistema_reservas_copia.model.Espacio;

public class espacioMapper {

    public static EspacioResponseDTO toResponseDTO(Espacio e, String mensaje) {
        return new EspacioResponseDTO(
                e.getIdEspacio(),
                e.getNombre(),
                e.getTipo(),
                e.getCapacidad(),
                e.getEstado(),
                mensaje
        );
    }

    public static Espacio toEntity(EspacioDTO dto) {
        Espacio e = new Espacio();
        e.setIdEspacio(dto.getId());
        e.setNombre(dto.getNombre());
        e.setTipo(dto.getTipo());
        e.setCapacidad(dto.getCapacidad());
        e.setEstado(dto.getEstado());
        return e;
    }
}
