package com.sistema_reservas_copia.controller.dto;

import lombok.Data;

@Data
public class LoginResponseDTO {
    private Long id;
    private String nombre;
    private String email;
    private String token;
    private String rol;


}



