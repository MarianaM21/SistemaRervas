package com.sistema_reservas_copia.dao;


import com.sistema_reservas_copia.model.Reserva;
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

    // Reservas activas de un usuario
    public List<Reserva> obtenerReservasActivasPorUsuario(Long idUsuario) {
        String jpql = "SELECT r FROM Reserva r WHERE r.usuario.id = :idUsuario AND r.estado = 'ACTIVA'";
        return entityManager.createQuery(jpql, Reserva.class)
                .setParameter("idUsuario", idUsuario)
                .getResultList();
    }

    // Pagos pendientes (si reservas tienen estado "PENDIENTE")
    public List<Reserva> obtenerPagosPendientes(Long idUsuario) {
        String jpql = "SELECT r FROM Reserva r WHERE r.usuario.id = :idUsuario AND r.estado = 'PENDIENTE'";
        return entityManager.createQuery(jpql, Reserva.class)
                .setParameter("idUsuario", idUsuario)
                .getResultList();
    }

    // Reservas futuras
    public List<Reserva> obtenerReservasFuturas(Long idUsuario) {
        String jpql = "SELECT r FROM Reserva r WHERE r.usuario.id = :idUsuario AND r.fechaFin > :hoy";
        return entityManager.createQuery(jpql, Reserva.class)
                .setParameter("idUsuario", idUsuario)
                .setParameter("hoy", LocalDateTime.now())
                .getResultList();
    }

    // Guardar reserva
    public void guardar(Reserva reserva) {
        entityManager.persist(reserva);
    }

    // Actualizar reserva
    public Reserva actualizar(Reserva reserva) {
        return entityManager.merge(reserva);
    }

    // Eliminar reserva
    public void eliminar(Long idReserva) {
        Reserva reserva = entityManager.find(Reserva.class, idReserva);
        if (reserva != null) {
            entityManager.remove(reserva);
        }
    }
}