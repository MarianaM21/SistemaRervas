package com.sistema_reservas.controller.dto;


import java.time.LocalDateTime;

public class ReservaDTO {
    private Long id_reserva;
    private Long usuarioId;
    private Long espacioId;
    private LocalDateTime fecha;
    private String estado;

<<<<<<< HEAD
    // Getters y setterss
    public Long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }
=======
    public Long getId_reserva() { return id_reserva; }
    public void setId_reserva(Long id_reserva) { this.id_reserva = id_reserva; }
>>>>>>> 63e50ff4accfb5703f927565bd8dff0ba70df549

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }

    public Long getEspacioId() { return espacioId; }
    public void setEspacioId(Long espacioId) { this.espacioId = espacioId; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
