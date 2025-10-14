package com.sistema_reservas.service;

import com.sistema_reservas.controller.dto.ReservaDTO;
import com.sistema_reservas.controller.dto.ReservaResponseDTO;
import com.sistema_reservas.dao.reservaDAO;
import com.sistema_reservas.mapper.reservaMapper;
import com.sistema_reservas.model.Espacio;
import com.sistema_reservas.model.Reserva;
import com.sistema_reservas.model.Usuario;
import com.sistema_reservas.repository.EspacioRepository;
import com.sistema_reservas.repository.ReservaRepository;
import com.sistema_reservas.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReservaServiceTest {

    @Mock
    private ReservaRepository reservaRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private EspacioRepository espacioRepository;
    @Mock
    private reservaDAO reservaDAO;

    @InjectMocks
    private ReservaService reservaService;

    private Usuario usuario;
    private Espacio espacio;
    private Reserva reserva;
    private ReservaDTO dto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Camilo");

        espacio = new Espacio();
        espacio.setIdEspacio(10L);
        espacio.setNombre("Sala 101");

        reserva = new Reserva();
        reserva.setId(100L);
        reserva.setUsuario(usuario);
        reserva.setEspacio(espacio);
        reserva.setEstado("ACTIVA");
        reserva.setFechaInicio(LocalDateTime.now());
        reserva.setFechaFin(LocalDateTime.now().plusHours(2));

        dto = new ReservaDTO();
        dto.setIdUsuario(1L);
        dto.setIdEspacio(10L);
        dto.setEstado("ACTIVA");
        dto.setFechaInicio(LocalDateTime.now());
        dto.setFechaFin(LocalDateTime.now().plusHours(2));
    }

    @Test
    void testCrearReserva_UsuarioNoExiste() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        ReservaResponseDTO response = reservaService.crearReserva(dto);

        assertEquals("El usuario no existe", response.getMensaje());
        verify(userRepository, times(1)).findById(1L);
        verifyNoInteractions(espacioRepository, reservaRepository);
    }

    @Test
    void testCrearReserva_EspacioNoExiste() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(espacioRepository.findById(10L)).thenReturn(Optional.empty());

        ReservaResponseDTO response = reservaService.crearReserva(dto);

        assertEquals("El espacio no existe", response.getMensaje());
        verify(userRepository).findById(1L);
        verify(espacioRepository).findById(10L);
        verifyNoInteractions(reservaRepository);
    }

    @Test
    void testCrearReserva_Exitosa() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(espacioRepository.findById(10L)).thenReturn(Optional.of(espacio));
        when(reservaRepository.save(any(Reserva.class))).thenReturn(reserva);

        ReservaResponseDTO response = reservaService.crearReserva(dto);

        assertNotNull(response);
        assertEquals("ACTIVA", response.getEstado());
        verify(reservaRepository, times(1)).save(any(Reserva.class));
    }

    @Test
    void testActualizarEstado_ReservaNoExiste() {
        when(reservaRepository.findById(100L)).thenReturn(Optional.empty());

        ReservaResponseDTO response = reservaService.actualizarEstado(100L, "CANCELADA");

        assertEquals("La reserva no existe", response.getMensaje());
        verify(reservaRepository, times(1)).findById(100L);
    }

    @Test
    void testActualizarEstado_Exitosa() {
        when(reservaRepository.findById(100L)).thenReturn(Optional.of(reserva));
        when(reservaRepository.save(any(Reserva.class))).thenReturn(reserva);

        ReservaResponseDTO response = reservaService.actualizarEstado(100L, "CANCELADA");

        assertNotNull(response);
        assertEquals("CANCELADA", response.getEstado());
        verify(reservaRepository, times(1)).save(reserva);
    }

    @Test
    void testEliminarReserva_NoExiste() {
        when(reservaRepository.findById(100L)).thenReturn(Optional.empty());

        String mensaje = reservaService.eliminarReserva(100L);

        assertEquals("La reserva no existe", mensaje);
        verify(reservaRepository, times(1)).findById(100L);
    }

    @Test
    void testEliminarReserva_Exitosa() {
        when(reservaRepository.findById(100L)).thenReturn(Optional.of(reserva));

        String mensaje = reservaService.eliminarReserva(100L);

        assertEquals("Reserva eliminada correctamente", mensaje);
        verify(reservaRepository, times(1)).deleteById(100L);
    }
}
