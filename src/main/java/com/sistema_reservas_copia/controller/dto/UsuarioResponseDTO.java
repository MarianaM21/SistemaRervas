package com.sistema_reservas_copia.controller.dto;

public class UsuarioResponseDTO {


        private Long id;
        private String nombre;
        private String email;
        private String mensaje;

        public UsuarioResponseDTO(Long id, String nombre, String email, String mensaje) {
            this.id = id;
            this.nombre = nombre;
            this.email = email;
            this.mensaje = mensaje;
        }

        // Getters
        public Long getId() { return id; }
        public String getNombre() { return nombre; }
        public String getEmail() { return email; }
        public String getMensaje() { return mensaje; }


}
