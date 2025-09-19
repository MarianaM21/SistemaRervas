package com.sistema_reservas.service;

import com.sistema_reservas.controller.dto.UserRegisterDTO;
import com.sistema_reservas.controller.dto.UserResponseDTO;
import com.sistema_reservas.model.Usuario;
import com.sistema_reservas.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceimpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserResponseDTO register(UserRegisterDTO dto) {
        System.out.println("📌 register() llamado con nombre: " + dto.getNombre());

        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setEmail(dto.getEmail());
        usuario.setPassword(dto.getPassword());
        usuario.setRol(dto.getRol());

        userRepository.save(usuario);

        return new UserResponseDTO(usuario.getId(), usuario.getNombre(), usuario.getEmail(), usuario.getRol());

    }

    // otros métodos...
}

