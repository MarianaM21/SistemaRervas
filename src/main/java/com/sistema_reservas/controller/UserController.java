package com.sistema_reservas.controller;

import com.sistema_reservas.controller.dto.UserLoginDTO;
import com.sistema_reservas.controller.dto.UserRegisterDTO;
import com.sistema_reservas.controller.dto.UserResponseRegisterDTO;
import com.sistema_reservas.controller.dto.LoginResponseDTO;
import com.sistema_reservas.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/registrar")
    public ResponseEntity<UserResponseRegisterDTO> registrar(@RequestBody UserRegisterDTO dto) {
        var resp = userService.registerUser(dto);
        return ResponseEntity.status(201).body(resp);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody UserLoginDTO dto) {
        var resp = userService.login(dto);
        return ResponseEntity.ok(resp);
    }
}
