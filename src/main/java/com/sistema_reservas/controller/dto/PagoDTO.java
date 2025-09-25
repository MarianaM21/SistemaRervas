package com.sistema_reservas.controller.dto;


public class PagoDTO {
    private Long id;
    private Long reservaId;
    private String metodo;    // tarjeta, PayU, Stripe
    private double monto;
    private String estado;    // APROBADO, FALLIDO, PENDIENTE
}
