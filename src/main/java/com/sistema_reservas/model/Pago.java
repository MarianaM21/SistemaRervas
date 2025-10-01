package com.sistema_reservas.model;


import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "pago")
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pago")
    private Long id;

    private double monto;
    private String estado; // Pendiente, Confirmado, Cancelado

    @ManyToOne
    @JoinColumn(name = "id_reserva")
    private Reserva reserva;

    private LocalDateTime fechaPago;

    private String metodo;

    public String getMetodo() { return metodo; }
    public void setMetodo(String metodo) { this.metodo = metodo; }


    public Pago() {}

    public Pago(double monto, String estado, Reserva reserva, LocalDateTime fechaPago) {
        this.monto = monto;
        this.estado = estado;
        this.metodo = metodo;
        this.reserva = reserva;
        this.fechaPago = fechaPago;

    }

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public double getMonto() { return monto; }
    public void setMonto(double monto) { this.monto = monto; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Reserva getReserva() { return reserva; }
    public void setReserva(Reserva reserva) { this.reserva = reserva; }

    public LocalDateTime getFechaPago() { return fechaPago; }
    public void setFechaPago(LocalDateTime fechaPago) { this.fechaPago = fechaPago; }
}
