package com.sistema_reservas.dao;

import com.sistema_reservas.model.Pago;
import org.springframework.stereotype.Repository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;

@Repository
@Transactional
public class pagoDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public Pago guardarPago(Pago pago) {
        if (pago.getId() == null) {
            entityManager.persist(pago);
            return pago;
        } else {
            return entityManager.merge(pago);
        }
    }

    public Pago obtenerPagoPorId(Long id) {
        try {
            return entityManager.createQuery(
                            "SELECT p FROM Pago p " +
                                    "JOIN FETCH p.reserva r " +
                                    "JOIN FETCH r.usuario u " +
                                    "JOIN FETCH r.espacio e " +
                                    "WHERE p.id = :id", Pago.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (jakarta.persistence.NoResultException e) {
            return null;
        }
    }


    public List<Pago> listarPagos() {
        return entityManager.createQuery(
                        "SELECT p FROM Pago p " +
                                "JOIN FETCH p.reserva r " +
                                "JOIN FETCH r.usuario u " +
                                "JOIN FETCH r.espacio e", Pago.class)
                .getResultList();
    }

    public Pago actualizar(Pago pago) {
        return entityManager.merge(pago);
    }

    public void eliminarPago(Long id) {
        Pago pago = entityManager.find(Pago.class, id);
        if (pago != null) {
            entityManager.remove(pago);
        }
    }
}

