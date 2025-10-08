package com.sistema_reservas.dao;

import com.sistema_reservas.model.Factura;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
@Transactional
public class facturaDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public Factura guardarFactura(Factura factura) {
        if (factura.getId() == null) {
            entityManager.persist(factura);
            return factura;
        } else {
            return entityManager.merge(factura);
        }
    }

    public Factura obtenerPorId(Long id) {
        return entityManager.find(Factura.class, id);
    }

    public List<Factura> listarFacturas() {
        return entityManager.createQuery("FROM Factura", Factura.class).getResultList();
    }

    public void eliminarFactura(Long id) {
        Factura factura = obtenerPorId(id);
        if (factura != null) {
            entityManager.remove(factura);
        }
    }
}

