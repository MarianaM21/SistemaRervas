package com.sistema_reservas.service;

import com.sistema_reservas.controller.dto.UserRegisterDTO;
import com.sistema_reservas.controller.dto.UserResponseDTO;

public interface UserService {
    UserResponseDTO register(UserRegisterDTO dto);
}
