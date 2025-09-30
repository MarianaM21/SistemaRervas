package com.sistema_reservas_copia.controller;

import com.sistema_reservas_copia.controller.dto.UserRegisterDTO;
import com.sistema_reservas_copia.controller.dto.UserResponseRegisterDTO;
import com.sistema_reservas_copia.controller.dto.UserLoginDTO;
import com.sistema_reservas_copia.controller.dto.LoginResponseDTO;
import com.sistema_reservas_copia.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponseRegisterDTO> register(@RequestBody UserRegisterDTO dto) {
        return ResponseEntity.ok(userService.registerUser(dto));
    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody UserLoginDTO dto) {
        LoginResponseDTO response = userService.login(dto);
        return ResponseEntity.ok(response);
    }
}
