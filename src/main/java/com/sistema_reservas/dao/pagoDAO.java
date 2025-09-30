package com.sistema_reservas_copia.dao;

import com.sistema_reservas_copia.model.Pago;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public class pagoDAO {

    @PersistenceContext
    private EntityManager entityManager;

    // Buscar pago por ID
    public Pago buscarPorId(Long id) {
        return entityManager.find(Pago.class, id);
    }

    // Pagos pendientes de una reserva
    public List<Pago> obtenerPagosPendientesPorReserva(Long idReserva) {
        String jpql = "SELECT p FROM Pago p WHERE p.reserva.id = :idReserva AND p.estado = 'PENDIENTE'";
        return entityManager.createQuery(jpql, Pago.class)
                .setParameter("idReserva", idReserva)
                .getResultList();
    }

    // Pagos confirmados de una reserva
    public List<Pago> obtenerPagosConfirmadosPorReserva(Long idReserva) {
        String jpql = "SELECT p FROM Pago p WHERE p.reserva.id = :idReserva AND p.estado = 'CONFIRMADO'";
        return entityManager.createQuery(jpql, Pago.class)
                .setParameter("idReserva", idReserva)
                .getResultList();
    }

    // Listar todos los pagos de un usuario (a través de reserva → usuario)
    public List<Pago> obtenerPagosPorUsuario(Long idUsuario) {
        String jpql = "SELECT p FROM Pago p WHERE p.reserva.usuario.id = :idUsuario";
        return entityManager.createQuery(jpql, Pago.class)
                .setParameter("idUsuario", idUsuario)
                .getResultList();
    }

    // Guardar un pago
    public void guardar(Pago pago) {
        entityManager.persist(pago);
    }

    // Actualizar un pago
    public Pago actualizar(Pago pago) {
        return entityManager.merge(pago);
    }

    // Eliminar un pago
    public void eliminar(Long id) {
        Pago pago = entityManager.find(Pago.class, id);
        if (pago != null) {
            entityManager.remove(pago);
        }
    }
}