package com.sistema_reservas.controller.dto;


public class ReservaDTO {
    private Long id;
    private Long espacioId;
    private Long usuarioId;
    private String fecha;       // Mejor usar LocalDate
    private String horaInicio;  // Mejor usar LocalTime
    private String horaFin;     // Mejor usar LocalTime
    private String estado;      // CONFIRMADA, CANCELADA, PENDIENTE_PAGO
}
