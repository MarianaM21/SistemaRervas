package com.sistema_reservas_copia.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserRegisterDTO {
    @NotBlank
    private String nombre;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    private String rol;

}



