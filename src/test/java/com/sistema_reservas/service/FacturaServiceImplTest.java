package com.sistema_reservas.service;

import com.sistema_reservas.dao.facturaDAO;
import com.sistema_reservas.model.Factura;
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

class FacturaServiceImplTest {

    @Mock
    private facturaDAO facturaDAO;

    @InjectMocks
    private FacturaServiceimpl facturaService;

    private Factura factura;
    private Pago pago;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        pago = new Pago();
        pago.setId(1L);
        pago.setMonto(200.0);

        factura = new Factura();
        factura.setId(1L);
        factura.setPago(pago);
        factura.setTotal(200.0);
        factura.setDescripcion("Factura de prueba");
        factura.setFechaGeneracion(LocalDateTime.now());
    }

    @Test
    void testGuardarFactura() {
        when(facturaDAO.guardarFactura(any(Factura.class))).thenReturn(factura);

        Factura resultado = facturaService.guardarFactura(factura);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(200.0, resultado.getTotal());
        verify(facturaDAO, times(1)).guardarFactura(factura);
    }

    @Test
    void testObtenerPorId() {
        when(facturaDAO.obtenerPorId(1L)).thenReturn(factura);

        Factura resultado = facturaService.obtenerPorId(1L);

        assertNotNull(resultado);
        assertEquals("Factura de prueba", resultado.getDescripcion());
        verify(facturaDAO, times(1)).obtenerPorId(1L);
    }

    @Test
    void testObtenerPorIdNoEncontrado() {
        when(facturaDAO.obtenerPorId(2L)).thenReturn(null);

        Factura resultado = facturaService.obtenerPorId(2L);

        assertNull(resultado);
        verify(facturaDAO, times(1)).obtenerPorId(2L);
    }

    @Test
    void testListarFacturas() {
        when(facturaDAO.listarFacturas()).thenReturn(Arrays.asList(factura));

        List<Factura> lista = facturaService.listarFacturas();

        assertNotNull(lista);
        assertEquals(1, lista.size());
        assertEquals("Factura de prueba", lista.get(0).getDescripcion());
        verify(facturaDAO, times(1)).listarFacturas();
    }

    @Test
    void testEliminarFactura() {
        doNothing().when(facturaDAO).eliminarFactura(1L);

        facturaService.eliminarFactura(1L);

        verify(facturaDAO, times(1)).eliminarFactura(1L);
    }
}
