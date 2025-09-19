package com.sistema_reservas.controller.dto;

import lombok.Data;

@Data
public class UserResponseDTO {
    private Long id;
    private String nombre;
    private String email;
    private String rol;

    public UserResponseDTO(Long id, String nombre, String email, String rol) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.rol = rol;
    }
}
