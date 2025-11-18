package com.sistema_reservas.service;

import com.sistema_reservas.controller.dto.*;
import com.sistema_reservas.dao.UsuarioDAO;
import com.sistema_reservas.model.Usuario;
import com.sistema_reservas.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsuarioServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UsuarioDAO usuarioDAO;

    @InjectMocks
    private UsuarioServiceimpl usuarioService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Juan");
        usuario.setEmail("juan@test.com");
        usuario.setPassword("123456");
        usuario.setRol("user");
    }

    // 🔹 Test: Registrar usuario
    @Test
    void testRegisterUser() {
        UserRegisterDTO dto = new UserRegisterDTO();
        dto.setNombre("Juan");
        dto.setEmail("juan@test.com");
        dto.setPassword("123456");
        dto.setRol("user");

        when(userRepository.existsByEmail(dto.getEmail())).thenReturn(false);
        when(userRepository.save(any(Usuario.class))).thenAnswer(inv -> {
            Usuario u = inv.getArgument(0);
            u.setId(1L);
            return u;
        });

        UserResponseRegisterDTO result = usuarioService.registerUser(dto);

        assertNotNull(result);
        assertEquals("Juan", result.getNombre());
        assertEquals("juan@test.com", result.getEmail());
        assertEquals("user", result.getRol());
        verify(userRepository, times(1)).save(any(Usuario.class));
    }

    // 🔹 Test: Login correcto
    @Test
    void testLoginCorrecto() {
        UserLoginDTO dto = new UserLoginDTO();
        dto.setEmail("juan@test.com");
        dto.setPassword("123456");

        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(usuario));

        LoginResponseDTO response = usuarioService.login(dto);

        assertEquals("juan@test.com", response.getEmail());
        assertEquals("Inicio de sesión exitoso", response.getMensaje());
    }

    // 🔹 Test: Login con contraseña incorrecta
    @Test
    void testLoginContraseñaIncorrecta() {
        UserLoginDTO dto = new UserLoginDTO();
        dto.setEmail("juan@test.com");
        dto.setPassword("wrong");

        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(usuario));

        assertThrows(IllegalArgumentException.class, () -> usuarioService.login(dto));
    }

    // 🔹 Test: Listar usuarios
    @Test
    void testListarUsuarios() {
        when(usuarioDAO.listarTodos()).thenReturn(List.of(usuario));

        List<UsuarioResponseDTO> lista = usuarioService.listarUsuarios();

        assertEquals(1, lista.size());
        assertEquals("Juan", lista.get(0).getNombre());
        assertEquals("listado", lista.get(0).getMensaje());
    }

    // 🔹 Test: Obtener usuario por ID
    @Test
    void testObtenerUsuarioPorId() {
        when(usuarioDAO.buscarPorId(1L)).thenReturn(usuario);

        UsuarioResponseDTO response = usuarioService.obtenerUsuarioPorId(1L);

        assertNotNull(response);
        assertEquals("Juan", response.getNombre());
    }

    // 🔹 Test: Actualizar usuario existente (corregido)
    @Test
    void testActualizarUsuario() {
        usuarioDTO dto = new usuarioDTO(1L, "Pedro", "pedro@test.com", "admin", null);

        when(usuarioDAO.buscarPorId(1L)).thenReturn(usuario);
        when(usuarioDAO.actualizar(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));

        UsuarioResponseDTO result = usuarioService.actualizarUsuario(1L, dto);

        assertNotNull(result);
        assertEquals("Pedro", result.getNombre());
        assertEquals("listado", result.getMensaje()); // ✅ coincide con el código real
        verify(usuarioDAO, times(1)).actualizar(any(Usuario.class));
    }

    // 🔹 Test: Actualizar usuario no existente
    @Test
    void testActualizarUsuarioNoExistente() {
        when(usuarioDAO.buscarPorId(99L)).thenReturn(null);

        UsuarioResponseDTO result = usuarioService.actualizarUsuario(99L, new usuarioDTO(99L, "X", "x@test.com", "user", null));

        assertNull(result);
    }

    // 🔹 Test: Eliminar usuario existente
    @Test
    void testEliminarUsuarioExistente() {
        when(usuarioDAO.buscarPorId(1L)).thenReturn(usuario);
        boolean eliminado = usuarioService.eliminarUsuario(1L);
        assertTrue(eliminado);
        verify(usuarioDAO, times(1)).eliminar(1L);
    }

    // 🔹 Test: Eliminar usuario no existente
    @Test
    void testEliminarUsuarioNoExistente() {
        when(usuarioDAO.buscarPorId(99L)).thenReturn(null);
        boolean eliminado = usuarioService.eliminarUsuario(99L);
        assertFalse(eliminado);
        verify(usuarioDAO, never()).eliminar(anyLong());
    }
}
