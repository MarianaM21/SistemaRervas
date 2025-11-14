package com.sistema_reservas.service;

import com.sistema_reservas.controller.dto.*;
import com.sistema_reservas.dao.usuarioDAO;
import com.sistema_reservas.mapper.usuarioMapper;
import com.sistema_reservas.model.Usuario;
import com.sistema_reservas.repository.UserRepository;
import com.sistema_reservas.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


//autenticacion
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;


import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioServiceimpl implements UsuarioService {

    private final UserRepository userRepository;
    private final usuarioDAO usuarioDAO;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authManager;
    private final PasswordEncoder passwordEncoder;



    @Override
    public UserResponseRegisterDTO registerUser(UserRegisterDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El usuario ya existe");
        }

        Usuario user = new Usuario();
        user.setNombre(dto.getNombre());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRol(dto.getRol() != null ? dto.getRol().toUpperCase() : "USER");



        userRepository.save(user);

        UserResponseRegisterDTO response = new UserResponseRegisterDTO();
        response.setId(user.getId());
        response.setNombre(user.getNombre());
        response.setEmail(user.getEmail());
        response.setRol(user.getRol());


        return response;
    }

    @Override
    public LoginResponseDTO login(UserLoginDTO dto) {
        Usuario user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("EMAIL_NOT_FOUND"));
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new RuntimeException("INVALID_PASSWORD");
        }

        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(auth);

        // Genera el token
        String token = jwtUtil.generateToken(user.getEmail());

        LoginResponseDTO response = new LoginResponseDTO();
        response.setId(user.getId());
        response.setNombre(user.getNombre());
        response.setEmail(user.getEmail());
        response.setRol(user.getRol());
        response.setToken(token);
        response.setMensaje("Inicio de sesión exitoso");

        return response;
    }


    @Override
    public void OlvideContraseña(OlvideContraseñaDTO request) {
        try {
            Usuario usuario = usuarioDAO.buscarPorCorreo(request.getEmail());
            System.out.println("Simulando envío de correo de recuperación a: " + usuario.getEmail());
        } catch (Exception e) {
            throw new RuntimeException("No se encontró ningún usuario con ese correo");
        }
    }

    @Override
    public void RestaurarContraseña(RestaurarContraseñaDTO request) {
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Las contraseñas no coinciden");
        }

        Usuario usuario = usuarioDAO.buscarPorCorreo(request.getEmail());

        if (usuario == null) {
            throw new RuntimeException("No existe un usuario con ese correo");
        }

        try {
            usuario.setPassword(request.getNewPassword());
            usuarioDAO.actualizar(usuario);
        } catch (Exception e) {
            throw new RuntimeException("Error interno al actualizar la contraseña");
        }
    }
    private UsuarioResponseDTO mapToResponseDTO(Usuario usuario) {
        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getRol(),
                "listado"
        );
    }

    @Override
    public List<UsuarioResponseDTO> listarUsuarios() {
        List<Usuario> usuarios = usuarioDAO.listarTodos();
        return usuarios.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }


    @Override
    public UsuarioResponseDTO obtenerUsuarioPorId(Long id) {
        Usuario usuario = usuarioDAO.buscarPorId(id);
        if (usuario == null) return null;
        return mapToResponseDTO(usuario);
    }

    @Override
    public UsuarioResponseDTO actualizarUsuario(Long id, usuarioDTO dto) {
        Usuario usuario = usuarioDAO.buscarPorId(id);
        if (usuario == null) {
            return null;
        }

        //si el usuario existe se acualiza
        usuario.setNombre(dto.getNombre());
        usuario.setEmail(dto.getEmail());
        usuario.setRol(dto.getRol());
        Usuario actualizado = usuarioDAO.actualizar(usuario);
        return mapToResponseDTO(actualizado);
    }


    @Override
    public boolean eliminarUsuario(Long id) {
        Usuario usuario = usuarioDAO.buscarPorId(id);
        if (usuario != null) {
            usuarioDAO.eliminar(id);
            return true;
        }
        return false;
    }
}







