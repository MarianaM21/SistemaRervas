package com.sistema_reservas_copia.service;

import com.sistema_reservas_copia.controller.dto.EspacioDTO;
import com.sistema_reservas_copia.controller.dto.EspacioResponseDTO;

import java.util.List;

public interface EspacioService {
    EspacioResponseDTO guardarEspacio(EspacioDTO dto);
    List<EspacioResponseDTO> listarEspacios();
    EspacioResponseDTO actualizarEspacio(Long id, EspacioDTO dto);
    void eliminarEspacio(Long id);
    EspacioResponseDTO buscarPorId(Long id);
}
