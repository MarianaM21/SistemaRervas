package com.sistema_reservas.model;


import jakarta.persistence.*;

@Entity
@Table(name = "Espacio")
public class Espacio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_espacio")
    private Long id;

    private String nombre;

    private String tipo; // Enum mapeado como String

    private int capacidad;

    private String descripcion;

    private String estado; // disponible, mantenimiento, ocupado

    // Getters y Setters
}
