package com.sistema_reservas.service;

import com.sistema_reservas.controller.dto.*;

import java.util.List;

public interface UsuarioService {
    UserResponseRegisterDTO registerUser(UserRegisterDTO dto);
    LoginResponseDTO login(UserLoginDTO dto);
    void OlvideContraseña(OlvideContraseñaDTO request);
    void RestaurarContraseña(RestaurarContraseñaDTO request);



    List<UsuarioResponseDTO> listarUsuarios();
    UsuarioResponseDTO obtenerUsuarioPorId(Long id);
    UsuarioResponseDTO actualizarUsuario(Long id, usuarioDTO usuarioDTO);
    boolean eliminarUsuario(Long id);
}
