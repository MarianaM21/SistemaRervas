package com.sistema_reservas.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RestaurarContraseñaDTO {
    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String newPassword;

    @NotBlank
    private String confirmPassword;
}
