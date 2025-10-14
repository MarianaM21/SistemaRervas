package com.sistema_reservas.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reserva")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reserva")
    private Long id_reserva;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "estado", nullable = false)
    private String estado;

    @Column(name = "Fecha", nullable = false)
    private LocalDateTime fecha;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "espacio_id", nullable = false)
    private Espacio espacio;



    public Reserva() {}

    // Getters y setters
    public Espacio getEspacio() { return espacio; }
    public void setEspacio(Espacio espacio) { this.espacio = espacio; }

    public Long getId_reserva() { return id_reserva; }
    public void setId_reserva(Long id_reserva) { this.id_reserva = id_reserva; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
}

