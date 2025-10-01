package com.sistema_reservas.controller.dto;

public class PagoDTO {
    private Long reservaId;
    private Double monto;
    private String metodo;

    public PagoDTO() {}

    public PagoDTO(Long reservaId, Double monto, String metodo) {
        this.reservaId = reservaId;
        this.monto = monto;
        this.metodo = metodo;
    }

    public Long getReservaId() { return reservaId; }
    public void setReservaId(Long reservaId) { this.reservaId = reservaId; }

    public Double getMonto() { return monto; }
    public void setMonto(Double monto) { this.monto = monto; }

    public String getMetodo() { return metodo; }
    public void setMetodo(String metodo) { this.metodo = metodo; }
}

