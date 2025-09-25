package com.sistema_reservas.model;



import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Pago")
public class Pago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pago")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_reserva")
    private Reserva reserva;

    private Double monto;

    private String metodo; // tarjeta, paypal, suscripcion

    @Column(name = "fecha_pago")
    private LocalDateTime fechaPago;

    private String estado; // pendiente, confirmado, fallido

    // Getters y Setters
}
