package com.sistema_reservas.service;

import com.sistema_reservas.controller.dto.UserRegisterDTO;
import com.sistema_reservas.controller.dto.UserResponseRegisterDTO;
import com.sistema_reservas.controller.dto.UserLoginDTO;
import com.sistema_reservas.controller.dto.LoginResponseDTO;
import com.sistema_reservas.model.Usuario;
import com.sistema_reservas.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceimpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserResponseRegisterDTO registerUser(UserRegisterDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("El usuario ya existe");
        }

        Usuario user = new Usuario();
        user.setNombre(dto.getNombre());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setRol(dto.getRol() != null ? dto.getRol() : "user");


        userRepository.save(user);

        UserResponseRegisterDTO response = new UserResponseRegisterDTO();
        response.setId(user.getId());
        response.setNombre(user.getNombre());
        response.setEmail(user.getEmail());
        return response;
    }

    @Override
    public LoginResponseDTO login(UserLoginDTO dto) {
        Usuario user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!user.getPassword().equals(dto.getPassword())) {
            throw new RuntimeException("Contraseña incorrecta");
        }


        LoginResponseDTO response = new LoginResponseDTO();
        response.setId(user.getId());
        response.setNombre(user.getNombre());
        response.setEmail(user.getEmail());
        response.setToken("TOKEN_DE_EJEMPLO");// opcional: JWT real
        response.setRol(user.getRol());

        // revisa si el rol llega correctamente
        System.out.println("ROL DEL USUARIO: " + user.getRol());
        return response;
    }
}




