package com.sistema_reservas.dao;

import com.sistema_reservas.model.Reserva;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional
public class reservaDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Reserva> listarReservas() {
        return entityManager.createQuery("FROM Reserva", Reserva.class).getResultList();
    }

    public Reserva obtenerPorId(Long id) {
        return entityManager.find(Reserva.class, id);
    }

    public Reserva guardarReserva(Reserva reserva) {
        if (reserva.getId_reserva() == null) {
            entityManager.persist(reserva);
            return reserva;
        } else {
            return entityManager.merge(reserva);
        }
    }

    public void eliminarReserva(Long id) {
        Reserva reserva = obtenerPorId(id);
        if (reserva != null) {
            entityManager.remove(reserva);
        }
    }
    public List<Reserva> listarPorEstadoYFecha(String estado, LocalDateTime fecha) {
        StringBuilder sb = new StringBuilder("FROM Reserva r WHERE 1=1 ");
        if (estado != null) sb.append("AND r.estado = :estado ");
        if (fecha != null) sb.append("AND r.fecha = :fecha ");

        var query = entityManager.createQuery(sb.toString(), Reserva.class);

        if (estado != null) query.setParameter("estado", estado);
        if (fecha != null) query.setParameter("fecha", fecha);

        return query.getResultList();
    }

    public List<Reserva> listarPorEmailUsuario(String email) {
        String jpql = "SELECT r FROM Reserva r WHERE r.usuario.email = :email ORDER BY r.fecha DESC";
        return entityManager.createQuery(jpql, Reserva.class)
                .setParameter("email", email)
                .getResultList();
    }


}
