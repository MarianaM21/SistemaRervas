package com.sistema_reservas_copia.mapper;

import com.sistema_reservas_copia.controller.dto.usuarioDTO;
import com.sistema_reservas_copia.model.usuarios;


public class usuarioMapper {

    // De entidad a DTO de respuesta
    public static usuarioDTO toDTO(usuarios user) {
        if (user == null) return null;
        return new usuarioDTO(
                user.getId(),
                user.getNombre(),
                user.getEmail(),
                user.getRol()
        );
    }

    // De DTO de respuesta a entidad
    public static usuarios toEntity(usuarioDTO dto) {
        if (dto == null) return null;
        usuarios user = new usuarios();
        user.setId(dto.getId());
        user.setNombre(dto.getNombre());
        user.setEmail(dto.getEmail());
        user.setRol(dto.getRol());
        return user;
    }
}
