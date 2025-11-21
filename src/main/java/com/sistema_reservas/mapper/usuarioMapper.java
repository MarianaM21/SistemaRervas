package com.sistema_reservas.mapper;

import com.sistema_reservas.controller.dto.usuarioDTO;
import com.sistema_reservas.controller.dto.UsuarioResponseDTO;
import com.sistema_reservas.model.Usuario;

public class usuarioMapper {

    // De entidad a usuarioDTO
    public static usuarioDTO toDTO(Usuario usuario) {
        if (usuario == null) return null;

        return new usuarioDTO(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getRol(),
                null, // mensaje
                usuario.getTelefono()
        );
    }

    // De usuarioDTO a Usuario
    public static Usuario toEntity(usuarioDTO dto) {
        if (dto == null) return null;

        Usuario usuario = new Usuario();
        usuario.setId(dto.getId());
        usuario.setNombre(dto.getNombre());
        usuario.setEmail(dto.getEmail());
        usuario.setRol(dto.getRol());
        usuario.setTelefono(dto.getTelefono());

        return usuario;
    }

    // Conversión a UsuarioResponseDTO
    public static UsuarioResponseDTO toResponseDTO(Usuario usuario, String mensaje) {
        if (usuario == null) return null;

        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getRol(),
                mensaje,
                usuario.getTelefono()
        );
    }
}
