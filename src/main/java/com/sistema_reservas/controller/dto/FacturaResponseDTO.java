package com.sistema_reservas.controller.dto;

import java.time.LocalDateTime;

public class FacturaResponseDTO {

    private Long id;
    private Long pagoId;
    private Double total;
    private String descripcion;
    private LocalDateTime fechaGeneracion;
    private String mensaje;

    // --- Getters y Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getPagoId() { return pagoId; }
    public void setPagoId(Long pagoId) { this.pagoId = pagoId; }

    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public LocalDateTime getFechaGeneracion() { return fechaGeneracion; }
    public void setFechaGeneracion(LocalDateTime fechaGeneracion) { this.fechaGeneracion = fechaGeneracion; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
}
