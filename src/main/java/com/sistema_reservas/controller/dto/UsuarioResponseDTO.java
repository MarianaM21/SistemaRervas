package com.sistema_reservas.controller.dto;

public class UsuarioResponseDTO {


        private Long id;
        private String nombre;
        private String email;
        private String rol;
        private String mensaje;
        private String telefono;

        public UsuarioResponseDTO(Long id, String nombre, String email,String rol, String mensaje, String telefono) {
            this.id = id;
            this.nombre = nombre;
            this.email = email;
            this.rol = rol;
            this.mensaje = mensaje;
            this.telefono = telefono;

        }

        // Getters
        public Long getId() { return id; }
        public String getNombre() { return nombre; }
        public String getEmail() { return email; }

        public String getRol() { return rol; }
        public String getMensaje() { return mensaje; }

        public String getTelefono() { return telefono; }

}
