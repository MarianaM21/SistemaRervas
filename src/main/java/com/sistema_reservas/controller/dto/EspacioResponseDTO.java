package com.sistema_reservas.controller.dto;

public class EspacioResponseDTO {

    private Long id;
    private String nombre;
    private String tipo;
    private int capacidad;
    private String estado;
    private String mensaje;

    // NUEVOS CAMPOS
    private String descripcion;
    private String ubicacion;
    private String caracteristicas;

    public EspacioResponseDTO(Long id, String nombre, String tipo,
                              int capacidad, String estado,
                              String descripcion,
                              String ubicacion,
                              String caracteristicas,
                              String mensaje) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.capacidad = capacidad;
        this.estado = estado;
        this.descripcion = descripcion;
        this.ubicacion = ubicacion;
        this.caracteristicas = caracteristicas;
        this.mensaje = mensaje;
    }

    // getters...
    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getTipo() { return tipo; }
    public int getCapacidad() { return capacidad; }
    public String getEstado() { return estado; }
    public String getDescripcion() { return descripcion; }
    public String getUbicacion() { return ubicacion; }
    public String getCaracteristicas() { return caracteristicas; }
    public String getMensaje() { return mensaje; }
}
