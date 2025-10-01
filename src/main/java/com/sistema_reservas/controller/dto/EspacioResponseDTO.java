package com.sistema_reservas.controller.dto;

    public class EspacioResponseDTO {

        private Long id;
        private String nombre;
        private String tipo;
        private int capacidad;
        private String estado;
        private String mensaje;

        public EspacioResponseDTO(Long id, String nombre, String tipo, int capacidad, String estado, String mensaje) {
            this.id = id;
            this.nombre = nombre;
            this.tipo = tipo;
            this.capacidad = capacidad;
            this.estado = estado;
            this.mensaje = mensaje;
        }

        // Getters
        public Long getId() {
            return id;
        }

        public String getNombre() {
            return nombre;
        }

        public String getTipo() {
            return tipo;
        }

        public int getCapacidad() {
            return capacidad;
        }

        public String getEstado() {
            return estado;
        }

        public String getMensaje() {
            return mensaje;
        }
    }

