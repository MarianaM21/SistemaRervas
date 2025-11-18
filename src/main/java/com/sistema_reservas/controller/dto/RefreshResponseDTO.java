package com.sistema_reservas.controller.dto;

public class RefreshResponseDTO {
    private String token;
    private String refreshToken;
    public RefreshResponseDTO(String token, String refreshToken) {
        this.token = token;
        this.refreshToken = refreshToken;
    }
    public String getToken() { return token; }
    public String getRefreshToken() { return refreshToken; }
}
