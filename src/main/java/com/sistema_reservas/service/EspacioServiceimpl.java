package com.sistema_reservas.service;

import com.sistema_reservas.controller.dto.EspacioResponseDTO;

import com.sistema_reservas.controller.dto.EspacioDTO;
import com.sistema_reservas.dao.espacioDAO;
import com.sistema_reservas.mapper.espacioMapper;
import com.sistema_reservas.model.Espacio;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EspacioServiceimpl implements EspacioService {

    private final espacioDAO espacioDAO;

    public EspacioServiceimpl(espacioDAO espacioDAO) {
        this.espacioDAO = espacioDAO;
    }

    @Override
    public EspacioResponseDTO guardarEspacio(EspacioDTO dto) {
        Espacio e = espacioMapper.toEntity(dto);
        espacioDAO.guardar(e);
        return espacioMapper.toResponseDTO(e, "Espacio guardado exitosamente");
    }

    @Override
    public List<EspacioResponseDTO> listarEspacios() {
        return espacioDAO.listarTodos().stream()
                .map(e -> espacioMapper.toResponseDTO(e, ""))
                .collect(Collectors.toList());
    }

    @Override
    public EspacioResponseDTO actualizarEspacio(Long id, EspacioDTO dto) {
        Espacio e = espacioDAO.buscarPorId(id);
        if (e == null) throw new RuntimeException("Espacio no encontrado");
        e.setNombre(dto.getNombre());
        e.setTipo(dto.getTipo());
        e.setCapacidad(dto.getCapacidad());
        e.setEstado(dto.getEstado());
        espacioDAO.actualizar(e);
        return espacioMapper.toResponseDTO(e, "Espacio actualizado exitosamente");
    }

    @Override
    public void eliminarEspacio(Long id) {
        espacioDAO.eliminar(id);
    }

    @Override
    public EspacioResponseDTO buscarPorId(Long id) {
        Espacio e = espacioDAO.buscarPorId(id);
        if (e == null) throw new RuntimeException("Espacio no encontrado");
        return espacioMapper.toResponseDTO(e, "");
    }
}
