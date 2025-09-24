package com.sistema_reservas.controller;

import com.sistema_reservas.controller.dto.UserRegisterDTO;
import com.sistema_reservas.controller.dto.UserResponseDTO;
import com.sistema_reservas.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;


    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> registerUser(@RequestBody UserRegisterDTO dto) {
        System.out.println("AuthController llamado con: " + dto.getEmail()); // <-- Para debug
        UserResponseDTO user = userService.register(dto);
        return ResponseEntity.status(201).body(user);
    }
}

