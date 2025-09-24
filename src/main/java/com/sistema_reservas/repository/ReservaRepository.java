package com.sistema_reservas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import com.sistema_reservas.model.Reserva;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    List<Reserva> findByUsuarioId(Long usuarioId);
    List<Reserva> findByEspacioId(Long espacioId);
}
