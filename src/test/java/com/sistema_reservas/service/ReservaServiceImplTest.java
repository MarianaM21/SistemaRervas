package com.sistema_reservas.service;

import com.sistema_reservas.dao.reservaDAO;
import com.sistema_reservas.model.Reserva;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReservaServiceimplTest {

    @Mock
    private reservaDAO reservaDAO;

    @InjectMocks
    private ReservaServiceimpl reservaService;

    private Reserva reserva1;
    private Reserva reserva2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        reserva1 = new Reserva();
        reserva1.setId_reserva(1L);
        reserva1.setEstado("ACTIVA");
        reserva1.setFecha(LocalDateTime.now());

        reserva2 = new Reserva();
        reserva2.setId_reserva(2L);
        reserva2.setEstado("CANCELADA");
        reserva2.setFecha(LocalDateTime.now().minusDays(1));
    }

    @Test
    void testListarReservas() {
        when(reservaDAO.listarReservas()).thenReturn(Arrays.asList(reserva1, reserva2));

        List<Reserva> reservas = reservaService.listarReservas();

        assertNotNull(reservas);
        assertEquals(2, reservas.size());
        verify(reservaDAO, times(1)).listarReservas();
    }

    @Test
    void testObtenerReservaPorId() {
        when(reservaDAO.obtenerPorId(1L)).thenReturn(reserva1);

        Reserva result = reservaService.obtenerReservaPorId(1L);

        assertNotNull(result);
        assertEquals("ACTIVA", result.getEstado());
        verify(reservaDAO, times(1)).obtenerPorId(1L);
    }


    @Test
    void testGuardarReserva() {
        when(reservaDAO.guardarReserva(reserva1)).thenReturn(reserva1);

        Reserva result = reservaService.guardarReserva(reserva1);

        assertNotNull(result);
        assertEquals(1L, result.getId_reserva());
        verify(reservaDAO, times(1)).guardarReserva(reserva1);
    }

    @Test
    void testEliminarReserva() {
        doNothing().when(reservaDAO).eliminarReserva(1L);

        reservaService.eliminarReserva(1L);

        verify(reservaDAO, times(1)).eliminarReserva(1L);
    }

    @Test
    void testListarReservasPorEstadoYFecha() {
        when(reservaDAO.listarPorEstadoYFecha(eq("ACTIVA"), any(LocalDateTime.class)))
                .thenReturn(List.of(reserva1));

        List<Reserva> result = reservaService.listarReservasPorEstadoYFecha("ACTIVA", "2025-10-05T12:00:00");

        assertEquals(1, result.size());
        verify(reservaDAO, times(1))
                .listarPorEstadoYFecha(eq("ACTIVA"), any(LocalDateTime.class));
    }
}
