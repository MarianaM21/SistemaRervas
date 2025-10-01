package com.sistema_reservas.controller.dto;

import java.time.LocalDateTime;

public class ReservaResponseDTO {

    private Long id;
    private Long idUsuario;
    private Long idEspacio;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private String estado;
    private String mensaje;

    public ReservaResponseDTO(Long id, Long idUsuario, Long idEspacio,
                              LocalDateTime fechaInicio, LocalDateTime fechaFin,
                              String estado, String mensaje) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.idEspacio = idEspacio;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.estado = estado;
        this.mensaje = mensaje;
    }

    // Getters
    public Long getId() { return id; }
    public Long getIdUsuario() { return idUsuario; }
    public Long getIdEspacio() { return idEspacio; }
    public LocalDateTime getFechaInicio() { return fechaInicio; }
    public LocalDateTime getFechaFin() { return fechaFin; }
    public String getEstado() { return estado; }
    public String getMensaje() { return mensaje; }
}
