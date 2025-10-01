package com.sistema_reservas.mapper;

import com.sistema_reservas.controller.dto.usuarioDTO;
import com.sistema_reservas.model.Usuario;


public class usuarioMapper {

    // De entidad a DTO de respuesta
    public static usuarioDTO toDTO(Usuario user) {
        if (user == null) return null;
        return new usuarioDTO(
                user.getId(),
                user.getNombre(),
                user.getEmail(),
                user.getRol()
        );
    }

    // De DTO de respuesta a entidad
    public static Usuario toEntity(usuarioDTO dto) {
        if (dto == null) return null;
        Usuario user = new Usuario();
        user.setId(dto.getId());
        user.setNombre(dto.getNombre());
        user.setEmail(dto.getEmail());
        user.setRol(dto.getRol());
        return user;
    }
}
