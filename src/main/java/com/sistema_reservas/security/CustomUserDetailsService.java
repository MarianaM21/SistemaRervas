package com.sistema_reservas.security;

import com.sistema_reservas.model.Usuario;
import com.sistema_reservas.repository.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));

        // Normalizar rol (mayúsculas, sin tildes ni variaciones)
        String rol = usuario.getRol().toUpperCase().trim();

        if (!rol.equals("ADMIN") && !rol.equals("USER") && !rol.equals("AFILIADO")) {
            throw new UsernameNotFoundException("Rol no válido para el usuario: " + rol);
        }

        return User.builder()
                .username(usuario.getEmail())
                .password(usuario.getPassword())
                .roles(rol)
                .build();
    }
}
