package com.sistema_reservas.service;

import com.sistema_reservas.controller.dto.EspacioDTO;
import com.sistema_reservas.controller.dto.EspacioResponseDTO;

import java.util.List;

public interface EspacioService {
    EspacioResponseDTO guardarEspacio(EspacioDTO dto);
    List<EspacioResponseDTO> listarEspacios();
    EspacioResponseDTO actualizarEspacio(Long id, EspacioDTO dto);
    void eliminarEspacio(Long id);
    EspacioResponseDTO buscarPorId(Long id);
}
