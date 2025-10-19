package com.sistema_reservas.service;

import com.sistema_reservas.controller.dto.*;
import com.sistema_reservas.dao.usuarioDAO;
import com.sistema_reservas.model.Usuario;
import com.sistema_reservas.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsuarioServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private usuarioDAO usuarioDAO;

    @InjectMocks
    private UsuarioServiceimpl usuarioService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Camilo Vega");
        usuario.setEmail("camilo@example.com");
        usuario.setPassword("1234");
        usuario.setRol("user");
    }

    // ✅ Test registrar usuario
    @Test
    void testRegisterUser_Success() {
        UserRegisterDTO dto = new UserRegisterDTO();
        dto.setNombre("Camilo");
        dto.setEmail("camilo@example.com");
        dto.setPassword("1234");
        dto.setRol("user");

        when(userRepository.existsByEmail(dto.getEmail())).thenReturn(false);
        when(userRepository.save(any(Usuario.class))).thenAnswer(inv -> {
            Usuario saved = inv.getArgument(0);
            saved.setId(1L);
            return saved;
        });

        UserResponseRegisterDTO response = usuarioService.registerUser(dto);

        assertNotNull(response);
        assertEquals("Camilo", response.getNombre());
        verify(userRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    void testRegisterUser_UserAlreadyExists() {
        UserRegisterDTO dto = new UserRegisterDTO();
        dto.setEmail("camilo@example.com");
        dto.setPassword("1234");

        when(userRepository.existsByEmail(dto.getEmail())).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> usuarioService.registerUser(dto));

        assertEquals("El usuario ya existe", exception.getMessage());
    }

    // ✅ Test login correcto
    @Test
    void testLogin_Success() {
        UserLoginDTO dto = new UserLoginDTO();
        dto.setEmail("camilo@example.com");
        dto.setPassword("1234");

        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(usuario));

        LoginResponseDTO response = usuarioService.login(dto);

        assertNotNull(response);
        assertEquals("camilo@example.com", response.getEmail());
        verify(userRepository, times(1)).findByEmail(dto.getEmail());
    }

    // ❌ Test login - contraseña incorrecta
    @Test
    void testLogin_PasswordIncorrect() {
        UserLoginDTO dto = new UserLoginDTO();
        dto.setEmail("camilo@example.com");
        dto.setPassword("wrong");

        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(usuario));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> usuarioService.login(dto));

        assertEquals("Contraseña incorrecta", exception.getMessage());
    }

    // ❌ Test login - usuario no encontrado
    @Test
    void testLogin_UserNotFound() {
        UserLoginDTO dto = new UserLoginDTO();
        dto.setEmail("noexiste@example.com");
        dto.setPassword("1234");

        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> usuarioService.login(dto));
        assertEquals("Usuario no encontrado", exception.getMessage());
    }

    // ✅ Test listar usuarios
    @Test
    void testListarUsuarios() {
        when(usuarioDAO.listarTodos()).thenReturn(List.of(usuario));

        List<UsuarioResponseDTO> lista = usuarioService.listarUsuarios();

        assertEquals(1, lista.size());
        assertEquals("Camilo Vega", lista.get(0).getNombre());
        verify(usuarioDAO, times(1)).listarTodos();
    }

    // ✅ Test obtener usuario por ID
    @Test
    void testObtenerUsuarioPorId() {
        when(usuarioDAO.buscarPorId(1L)).thenReturn(usuario);

        UsuarioResponseDTO result = usuarioService.obtenerUsuarioPorId(1L);

        assertNotNull(result);
        assertEquals("Camilo Vega", result.getNombre());
    }

    // ❌ Test obtener usuario no existente
    @Test
    void testObtenerUsuarioPorId_NotFound() {
        when(usuarioDAO.buscarPorId(1L)).thenReturn(null);

        UsuarioResponseDTO result = usuarioService.obtenerUsuarioPorId(1L);
        assertNull(result);
    }

    // ✅ Test actualizar usuario
    @Test
    void testActualizarUsuario() {
        usuarioDTO dto = new usuarioDTO(1L, "Alex", "alex@example.com", "admin", null);

        when(usuarioDAO.buscarPorId(1L)).thenReturn(usuario);
        when(usuarioDAO.actualizar(any(Usuario.class))).thenReturn(usuario);

        UsuarioResponseDTO result = usuarioService.actualizarUsuario(1L, dto);

        assertNotNull(result);
        assertEquals("Usuario actualizado correctamente", result.getMensaje());
        verify(usuarioDAO, times(1)).actualizar(any(Usuario.class));
    }

    // ✅ Test eliminar usuario
    @Test
    void testEliminarUsuario_Success() {
        when(usuarioDAO.buscarPorId(1L)).thenReturn(usuario);

        boolean eliminado = usuarioService.eliminarUsuario(1L);

        assertTrue(eliminado);
        verify(usuarioDAO, times(1)).eliminar(1L);
    }

    @Test
    void testEliminarUsuario_NotFound() {
        when(usuarioDAO.buscarPorId(1L)).thenReturn(null);

        boolean eliminado = usuarioService.eliminarUsuario(1L);

        assertFalse(eliminado);
        verify(usuarioDAO, never()).eliminar(anyLong());
    }
}
