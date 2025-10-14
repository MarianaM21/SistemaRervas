package com.sistema_reservas.controller.dto;


import java.time.LocalDateTime;

public class ReservaResponseDTO {
    private Long id;
    private Long idUsuario;
    private String nombreUsuario;
    private Long idEspacio;
    private String nombreEspacio;
    private LocalDateTime fecha;
    private String estado;
    private String mensajeError;

    public ReservaResponseDTO(Long id, Long idUsuario, String nombreUsuario,
                              Long idEspacio, String nombreEspacio, LocalDateTime fecha, String estado) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.idEspacio = idEspacio;
        this.nombreEspacio = nombreEspacio;
        this.fecha = fecha;
        this.estado = estado;
    }

    public ReservaResponseDTO(String mensajeError) {
        this.mensajeError = mensajeError;
    }

    // Getters
    public Long getId() { return id; }
    public Long getIdUsuario() { return idUsuario; }
    public String getNombreUsuario() { return nombreUsuario; }
    public Long getIdEspacio() { return idEspacio; }
    public String getNombreEspacio() { return nombreEspacio; }
    public LocalDateTime getFecha() { return fecha; }
    public String getEstado() { return estado; }
    public String getMensajeError() { return mensajeError; }
}
