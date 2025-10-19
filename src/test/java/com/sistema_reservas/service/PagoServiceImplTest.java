package com.sistema_reservas.service;

import com.sistema_reservas.dao.pagoDAO;
import com.sistema_reservas.model.Pago;
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

class PagoServiceImplTest {

    @Mock
    private pagoDAO pagoDAO;

    @InjectMocks
    private PagoServiceimpl pagoService;

    private Pago pago;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        pago = new Pago();
        pago.setId(1L);
        pago.setMonto(150000);
        pago.setEstado("Confirmado");
        pago.setMetodo("Tarjeta");
        pago.setFechaPago(LocalDateTime.now());
    }

    @Test
    void testGuardarPago() {
        when(pagoDAO.guardarPago(pago)).thenReturn(pago);

        Pago resultado = pagoService.guardarPago(pago);

        assertNotNull(resultado);
        assertEquals("Confirmado", resultado.getEstado());
        verify(pagoDAO, times(1)).guardarPago(pago);
    }

    @Test
    void testListarPagos() {
        List<Pago> lista = Arrays.asList(pago);
        when(pagoDAO.listarPagos()).thenReturn(lista);

        List<Pago> resultado = pagoService.listarPagos();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        verify(pagoDAO, times(1)).listarPagos();
    }

    @Test
    void testObtenerPagoPorId() {
        when(pagoDAO.obtenerPagoPorId(1L)).thenReturn(pago);

        Pago resultado = pagoService.obtenerPagoPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(pagoDAO, times(1)).obtenerPagoPorId(1L);
    }

    @Test
    void testActualizarPago() {
        when(pagoDAO.actualizar(pago)).thenReturn(pago);

        Pago resultado = pagoService.actualizarPago(pago);

        assertNotNull(resultado);
        assertEquals("Confirmado", resultado.getEstado());
        verify(pagoDAO, times(1)).actualizar(pago);
    }

    @Test
    void testEliminarPago() {
        doNothing().when(pagoDAO).eliminarPago(1L);

        pagoService.eliminarPago(1L);

        verify(pagoDAO, times(1)).eliminarPago(1L);
    }
}
