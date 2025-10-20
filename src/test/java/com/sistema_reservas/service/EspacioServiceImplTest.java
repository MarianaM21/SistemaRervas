package com.sistema_reservas.service;

import com.sistema_reservas.controller.dto.EspacioDTO;
import com.sistema_reservas.controller.dto.EspacioResponseDTO;
import com.sistema_reservas.dao.espacioDAO;
import com.sistema_reservas.mapper.espacioMapper;
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

        espacio = new Espacio("Sala A", "Reunión", 10, "Disponible");
        espacio.setIdEspacio(1L);

        dto = new EspacioDTO();
        dto.setId(1L);
        dto.setNombre("Sala A");
        dto.setTipo("Reunión");
        dto.setCapacidad(10);
        dto.setEstado("Disponible");
    }

    @Test
    void guardarEspacio_deberiaGuardarYRetornarDTO() {
        when(espacioDAO.guardar(any(Espacio.class))).thenReturn(espacio);

        EspacioResponseDTO result = espacioService.guardarEspacio(dto);

        assertNotNull(result);
        assertEquals("Sala A", result.getNombre());
        assertEquals("Espacio guardado exitosamente", result.getMensaje());
        verify(espacioDAO, times(1)).guardar(any(Espacio.class));
    }

    @Test
    void listarEspacios_deberiaRetornarListaDeEspacios() {
        when(espacioDAO.listarTodos()).thenReturn(Arrays.asList(espacio));

        List<EspacioResponseDTO> result = espacioService.listarEspacios();

        assertEquals(1, result.size());
        assertEquals("Sala A", result.get(0).getNombre());
        verify(espacioDAO, times(1)).listarTodos();
    }

    @Test
    void actualizarEspacio_deberiaActualizarCuandoExiste() {
        when(espacioDAO.buscarPorId(1L)).thenReturn(espacio);
        when(espacioDAO.actualizar(any(Espacio.class))).thenReturn(espacio);

        dto.setNombre("Sala B");
        EspacioResponseDTO result = espacioService.actualizarEspacio(1L, dto);

        assertEquals("Sala B", result.getNombre());
        assertEquals("Espacio actualizado exitosamente", result.getMensaje());
        verify(espacioDAO).actualizar(any(Espacio.class));
    }

    @Test
    void actualizarEspacio_deberiaLanzarExcepcionSiNoExiste() {
        when(espacioDAO.buscarPorId(1L)).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                espacioService.actualizarEspacio(1L, dto));

        assertEquals("Espacio no encontrado", exception.getMessage());
        verify(espacioDAO, never()).actualizar(any(Espacio.class));
    }

    @Test
    void eliminarEspacio_deberiaEliminarCuandoExiste() {
        when(espacioDAO.buscarPorId(1L)).thenReturn(espacio);

        espacioService.eliminarEspacio(1L);

        verify(espacioDAO, times(1)).eliminar(1L);
    }

    @Test
    void eliminarEspacio_deberiaLanzarExcepcionSiNoExiste() {
        when(espacioDAO.buscarPorId(1L)).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                espacioService.eliminarEspacio(1L));

        assertEquals("Espacio no encontrado para eliminar", exception.getMessage());
        verify(espacioDAO, never()).eliminar(anyLong());
    }

    @Test
    void buscarPorId_deberiaRetornarEspacioSiExiste() {
        when(espacioDAO.buscarPorId(1L)).thenReturn(espacio);

        EspacioResponseDTO result = espacioService.buscarPorId(1L);

        assertNotNull(result);
        assertEquals("Sala A", result.getNombre());
        verify(espacioDAO).buscarPorId(1L);
    }

    @Test
    void buscarPorId_deberiaLanzarExcepcionSiNoExiste() {
        when(espacioDAO.buscarPorId(1L)).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                espacioService.buscarPorId(1L));

        assertEquals("Espacio no encontrado", exception.getMessage());
    }
}
