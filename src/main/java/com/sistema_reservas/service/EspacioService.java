package com.sistema_reservas.service;

import com.sistema_reservas.model.Espacio;
import com.sistema_reservas.repository.EspacioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EspacioService {

    private final EspacioRepository espacioRepository;

    public EspacioService(EspacioRepository espacioRepository) {
        this.espacioRepository = espacioRepository;
    }

    public List<Espacio> listarEspacios() {
        return espacioRepository.findAll();
    }

    public Optional<Espacio> buscarPorId(Long id) {
        return espacioRepository.findById(id);
    }

    public List<Espacio> buscarPorTipo(String tipo) {
        return espacioRepository.findByTipo(tipo);
    }

    public List<Espacio> buscarPorEstado(String estado) {
        return espacioRepository.findByEstado(estado);
    }

    public Espacio guardarEspacio(Espacio espacio) {
        return espacioRepository.save(espacio);
    }

    public void eliminarEspacio(Long id) {
        espacioRepository.deleteById(id);
    }
}
