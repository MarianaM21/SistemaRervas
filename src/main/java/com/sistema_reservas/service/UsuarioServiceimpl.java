package com.sistema_reservas.service;

import com.sistema_reservas.controller.dto.*;
import com.sistema_reservas.dao.usuarioDAO;
import com.sistema_reservas.mapper.usuarioMapper;
import com.sistema_reservas.model.Usuario;
import com.sistema_reservas.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioServiceimpl implements UsuarioService {

    private final UserRepository userRepository;
    private final usuarioDAO usuarioDAO;


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

        // revisa si el rol llega correctamente a postam
        System.out.println("ROL DEL USUARIO: " + user.getRol());
        return response;
    }

    @Override
    public void OlvideContraseña(OlvideContraseñaDTO request) {
        try {
            Usuario usuario = usuarioDAO.buscarPorCorreo(request.getEmail());
            System.out.println("📩 Simulando envío de correo de recuperación a: " + usuario.getEmail());
            // Aquí podrías implementar JavaMailSender más adelante.
        } catch (Exception e) {
            throw new RuntimeException("No se encontró ningún usuario con ese correo");
        }
    }

    @Override
    public void RestaurarContraseña(RestaurarContraseñaDTO request) {
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Las contraseñas no coinciden");
        }

        try {
            Usuario usuario = usuarioDAO.buscarPorCorreo(request.getEmail());

            if (usuario == null) {
                throw new RuntimeException("No existe un usuario con ese correo");
            }

            usuario.setPassword(request.getNewPassword());
            usuarioDAO.actualizar(usuario);
        } catch (Exception e) {
            throw new RuntimeException("No se pudo restablecer la contraseña. Verifica el correo.");
        }
    }
    private UsuarioResponseDTO mapToResponseDTO(Usuario usuario) {
        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getRol(),        // campo rol
                "Usuario actualizado correctamente"
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
        usuario.setNombre(dto.getNombre());
        usuario.setEmail(dto.getEmail());
        usuario.setRol(dto.getRol());
        Usuario actualizado = usuarioDAO.actualizar(usuario);
        return usuarioMapper.toResponseDTO(actualizado, "Usuario actualizado correctamente");

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







