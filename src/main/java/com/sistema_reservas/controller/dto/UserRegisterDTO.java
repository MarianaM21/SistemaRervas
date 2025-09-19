package com.sistema_reservas.controller.dto;

import lombok.Data;

@Data
public class UserRegisterDTO {
    private String nombre;
    private String email;
    private String password;
    private String rol; // ADMIN, AFILIADO, USER
}
