package com.sistema_reservas.service;

import com.sistema_reservas.model.Usuario;
import com.sistema_reservas.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {

    private final UserRepository userRepository;

    public UsuarioService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Usuario registrarUsuario(Usuario usuario) {
        if (userRepository.existsByEmail(usuario.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }
        return userRepository.save(usuario);
    }

    public Optional<Usuario> buscarPorId(Long id) {
        return userRepository.findById(id);
    }

    public boolean existePorEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public Usuario actualizarUsuario(Usuario usuario) {
        return userRepository.save(usuario);
    }

    public void eliminarUsuario(Long id) {
        userRepository.deleteById(id);
    }
}
