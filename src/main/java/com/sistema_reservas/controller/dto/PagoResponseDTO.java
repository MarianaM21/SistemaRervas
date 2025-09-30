package com.sistema_reservas_copia.controller.dto;

import java.time.LocalDateTime;

public class PagoResponseDTO {
    private Long id;
    private Long reservaId;
    private Double monto;
    private String metodo;
    private String estado;
    private LocalDateTime fechaPago;
    private String mensaje;

    public PagoResponseDTO(Long id, Long reservaId, Double monto, String metodo, String estado, LocalDateTime fechaPago, String mensaje) {
        this.id = id;
        this.reservaId = reservaId;
        this.monto = monto;
        this.metodo = metodo;
        this.estado = estado;
        this.fechaPago = fechaPago;
        this.mensaje = mensaje;
    }

    public Long getId() { return id; }
    public Long getReservaId() { return reservaId; }
    public Double getMonto() { return monto; }
    public String getMetodo() { return metodo; }
    public String getEstado() { return estado; }
    public LocalDateTime getFechaPago() { return fechaPago; }
    public String getMensaje() { return mensaje; }
}
