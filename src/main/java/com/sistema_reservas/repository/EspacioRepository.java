package com.sistema_reservas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import com.sistema_reservas.model.Espacio;

public interface EspacioRepository extends JpaRepository<Espacio, Long> {
    List<Espacio> findByTipo(String tipo);
    List<Espacio> findByEstado(String estado);
}
