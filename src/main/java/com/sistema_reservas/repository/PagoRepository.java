package com.sistema_reservas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import com.sistema_reservas.model.Pago;

public interface PagoRepository extends JpaRepository<Pago, Long> {
    List<Pago> findByReservaId(Long reservaId);
    List<Pago> findByEstado(String estado);
}
