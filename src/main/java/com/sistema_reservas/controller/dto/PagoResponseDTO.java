package com.sistema_reservas.controller.dto;

import java.time.LocalDateTime;

public class PagoResponseDTO {
    private Long id;
    private double monto;
    private String estado;
    private Long reservaId;
    private LocalDateTime fechaPago;
    private String metodo;
    private String nombreUsuario;
    private String nombreEspacio;
    private String mensaje;

    public PagoResponseDTO() {}

    public PagoResponseDTO(String mensaje) { this.mensaje = mensaje; }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public double getMonto() { return monto; }
    public void setMonto(double monto) { this.monto = monto; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Long getReservaId() { return reservaId; }
    public void setReservaId(Long reservaId) { this.reservaId = reservaId; }

    public LocalDateTime getFechaPago() { return fechaPago; }
    public void setFechaPago(LocalDateTime fechaPago) { this.fechaPago = fechaPago; }

    public String getMetodo() { return metodo; }
    public void setMetodo(String metodo) { this.metodo = metodo; }

    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }

    public String getNombreEspacio() { return nombreEspacio; }
    public void setNombreEspacio(String nombreEspacio) { this.nombreEspacio = nombreEspacio; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
}
