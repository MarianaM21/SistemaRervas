package com.sistema_reservas.mapper;

import com.sistema_reservas.controller.dto.EspacioDTO;
import com.sistema_reservas.controller.dto.EspacioResponseDTO;
import com.sistema_reservas.model.Espacio;

public class espacioMapper {

    public static EspacioResponseDTO toResponseDTO(Espacio e, String mensaje) {
        return new EspacioResponseDTO(
                e.getIdEspacio(),
                e.getNombre(),
                e.getTipo(),
                e.getCapacidad(),
                e.getEstado(),
                e.getDescripcion(),
                e.getUbicacion(),
                e.getCaracteristicas(),
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
        e.setDescripcion(dto.getDescripcion());
        e.setUbicacion(dto.getUbicacion());
        e.setCaracteristicas(dto.getCaracteristicas());
        return e;
    }
}
