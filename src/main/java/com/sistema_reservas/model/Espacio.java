package com.sistema_reservas.model;

import jakarta.persistence.*;

@Entity
@Table(name = "espacio")
public class Espacio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_espacio")
    private Long idEspacio;

    private String nombre;
    private String tipo;
    private int capacidad;
    private String estado;

    // NUEVOS CAMPOS
    private String descripcion;
    private String ubicacion;
    private String caracteristicas;

    public Espacio() {}

    public Espacio(String nombre, String tipo, int capacidad, String estado) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.capacidad = capacidad;
        this.estado = estado;
    }

    // Getters y setters
    public Long getIdEspacio() { return idEspacio; }
    public void setIdEspacio(Long idEspacio) { this.idEspacio = idEspacio; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public int getCapacidad() { return capacidad; }
    public void setCapacidad(int capacidad) { this.capacidad = capacidad; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }

    public String getCaracteristicas() { return caracteristicas; }
    public void setCaracteristicas(String caracteristicas) { this.caracteristicas = caracteristicas; }
}
