package com.sistema_reservas_copia.service;

import com.sistema_reservas_copia.controller.dto.UserRegisterDTO;
import com.sistema_reservas_copia.controller.dto.UserResponseRegisterDTO;
import com.sistema_reservas_copia.controller.dto.UserLoginDTO;
import com.sistema_reservas_copia.controller.dto.LoginResponseDTO;

public interface UserService {
    UserResponseRegisterDTO registerUser(UserRegisterDTO dto);
    LoginResponseDTO login(UserLoginDTO dto);

}
