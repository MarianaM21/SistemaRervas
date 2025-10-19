package com.sistema_reservas.service;

import com.sistema_reservas.controller.dto.LoginResponseDTO;
import com.sistema_reservas.controller.dto.UserLoginDTO;
import com.sistema_reservas.controller.dto.UserRegisterDTO;
import com.sistema_reservas.controller.dto.UserResponseRegisterDTO;
import com.sistema_reservas.model.Usuario;
import com.sistema_reservas.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceimpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // --- Test: registrar usuario exitoso ---
    @Test
    void testRegisterUser_Success() {
        UserRegisterDTO dto = new UserRegisterDTO();
        dto.setNombre("Alex");
        dto.setEmail("alex@test.com");
        dto.setPassword("1234");

        when(userRepository.existsByEmail(dto.getEmail())).thenReturn(false);

        Usuario savedUser = new Usuario();
        savedUser.setId(1L);
        savedUser.setNombre("Alex");
        savedUser.setEmail("alex@test.com");
        savedUser.setPassword("1234");
        savedUser.setRol("user");

        when(userRepository.save(any(Usuario.class))).thenReturn(savedUser);

        UserResponseRegisterDTO response = userService.registerUser(dto);

        assertNotNull(response);
        assertEquals("Alex", response.getNombre());
        assertEquals("alex@test.com", response.getEmail());
        assertEquals(1L, response.getId());
    }

    // --- Test: registrar usuario ya existente ---
    @Test
    void testRegisterUser_UserAlreadyExists() {
        UserRegisterDTO dto = new UserRegisterDTO();
        dto.setEmail("alex@test.com");

        when(userRepository.existsByEmail(dto.getEmail())).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            userService.registerUser(dto);
        });

        assertEquals("El usuario ya existe", ex.getMessage());
    }

    // --- Test: login exitoso ---
    @Test
    void testLogin_Success() {
        UserLoginDTO dto = new UserLoginDTO();
        dto.setEmail("alex@test.com");
        dto.setPassword("1234");

        Usuario user = new Usuario();
        user.setId(1L);
        user.setNombre("Alex");
        user.setEmail("alex@test.com");
        user.setPassword("1234");
        user.setRol("admin");

        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(user));

        LoginResponseDTO response = userService.login(dto);

        assertNotNull(response);
        assertEquals("Alex", response.getNombre());
        assertEquals("alex@test.com", response.getEmail());
        assertEquals("admin", response.getRol());
        assertEquals("TOKEN_DE_EJEMPLO", response.getToken());
    }

    // --- Test: login usuario no encontrado ---
    @Test
    void testLogin_UserNotFound() {
        UserLoginDTO dto = new UserLoginDTO();
        dto.setEmail("alex@test.com");
        dto.setPassword("1234");

        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            userService.login(dto);
        });

        assertEquals("Usuario no encontrado", ex.getMessage());
    }

    // --- Test: login contraseña incorrecta ---
    @Test
    void testLogin_IncorrectPassword() {
        UserLoginDTO dto = new UserLoginDTO();
        dto.setEmail("alex@test.com");
        dto.setPassword("wrong");

        Usuario user = new Usuario();
        user.setEmail("alex@test.com");
        user.setPassword("1234");

        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(user));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            userService.login(dto);
        });

        assertEquals("Contraseña incorrecta", ex.getMessage());
    }
}
