package com.sistema_reservas.controller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RestaurarContraseñaDTO {
    @NotBlank
    private String token;

    @NotBlank
    private String newPassword;

    @NotBlank
    private String confirmPassword;
}

