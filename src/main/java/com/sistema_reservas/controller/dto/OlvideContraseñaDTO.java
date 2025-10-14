package com.sistema_reservas.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OlvideContraseñaDTO {
    @Email
    @NotBlank
    private String email;
}
