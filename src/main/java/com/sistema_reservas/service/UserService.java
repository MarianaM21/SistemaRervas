package com.sistema_reservas.service;

import com.sistema_reservas.controller.dto.UserRegisterDTO;
import com.sistema_reservas.controller.dto.UserResponseRegisterDTO;
import com.sistema_reservas.controller.dto.UserLoginDTO;
import com.sistema_reservas.controller.dto.LoginResponseDTO;

public interface UserService {
    UserResponseRegisterDTO registerUser(UserRegisterDTO dto);
    LoginResponseDTO login(UserLoginDTO dto);

}
