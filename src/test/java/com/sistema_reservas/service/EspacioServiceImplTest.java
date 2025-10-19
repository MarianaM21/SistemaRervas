package com.sistema_reservas.service;

import com.sistema_reservas.controller.dto.EspacioDTO;
import com.sistema_reservas.controller.dto.EspacioResponseDTO;
import com.sistema_reservas.dao.espacioDAO;
import com.sistema_reservas.model.Espacio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EspacioServiceImplTest {

    @Mock
    private espacioDAO espacioDAO;

    @InjectMocks
    private EspacioServiceimpl espacioService;

    private Espacio espacio;
    private EspacioDTO dto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        espacio = new Espacio("Sala Principal", "Auditorio", 100, "Disponible");
        espacio.setIdEspacio(1L);

        dto = new EspacioDTO();
        dto.setId(1L);
        dto.setNombre("Sala Principal");
        dto.setTipo("Auditorio");
        dto.setCapacidad(100);
        dto.setEstado("Disponible");
    }

    @Test
    void testGuardarEspacio() {
        when(espacioDAO.guardar(any(Espacio.class))).thenReturn(espacio);

        EspacioResponseDTO response = espacioService.guardarEspacio(dto);

        assertNotNull(response);
        assertEquals("Sala Principal", response.getNombre());
        assertEquals("Espacio guardado exitosamente", response.getMensaje());
        verify(espacioDAO, times(1)).guardar(any(Espacio.class));
    }

    @Test
    void testListarEspacios() {
        when(espacioDAO.listarTodos()).thenReturn(Arrays.asList(espacio));

        List<EspacioResponseDTO> lista = espacioService.listarEspacios();

        assertNotNull(lista);
        assertEquals(1, lista.size());
        assertEquals("Sala Principal", lista.get(0).getNombre());
        verify(espacioDAO, times(1)).listarTodos();
    }

    @Test
    void testActualizarEspacio() {
        when(espacioDAO.buscarPorId(1L)).thenReturn(espacio);
        when(espacioDAO.actualizar(any(Espacio.class))).thenReturn(espacio);

        dto.setNombre("Sala Modificada");
        EspacioResponseDTO response = espacioService.actualizarEspacio(1L, dto);

        assertNotNull(response);
        assertEquals("Sala Modificada", response.getNombre());
        assertEquals("Espacio actualizado exitosamente", response.getMensaje());
        verify(espacioDAO, times(1)).buscarPorId(1L);
        verify(espacioDAO, times(1)).actualizar(any(Espacio.class));
    }

    @Test
    void testActualizarEspacioNoEncontrado() {
        when(espacioDAO.buscarPorId(1L)).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                espacioService.actualizarEspacio(1L, dto)
        );

        assertEquals("Espacio no encontrado", exception.getMessage());
        verify(espacioDAO, times(1)).buscarPorId(1L);
        verify(espacioDAO, never()).actualizar(any(Espacio.class));
    }

    @Test
    void testEliminarEspacio() {
        doNothing().when(espacioDAO).eliminar(1L);

        espacioService.eliminarEspacio(1L);

        verify(espacioDAO, times(1)).eliminar(1L);
    }

    @Test
    void testBuscarPorId() {
        when(espacioDAO.buscarPorId(1L)).thenReturn(espacio);

        EspacioResponseDTO response = espacioService.buscarPorId(1L);

        assertNotNull(response);
        assertEquals("Sala Principal", response.getNombre());
        verify(espacioDAO, times(1)).buscarPorId(1L);
    }

    @Test
    void testBuscarPorIdNoEncontrado() {
        when(espacioDAO.buscarPorId(1L)).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                espacioService.buscarPorId(1L)
        );

        assertEquals("Espacio no encontrado", exception.getMessage());
        verify(espacioDAO, times(1)).buscarPorId(1L);
    }
}
