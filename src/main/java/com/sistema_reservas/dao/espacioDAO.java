package com.sistema_reservas_copia.dao;

import com.sistema_reservas_copia.model.Espacio;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public class espacioDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public Espacio guardar(Espacio espacio) {
        entityManager.persist(espacio);
        return espacio;
    }

    public Espacio actualizar(Espacio espacio) {
        return entityManager.merge(espacio);
    }

    public void eliminar(Long id) {
        Espacio espacio = entityManager.find(Espacio.class, id);
        if (espacio != null) entityManager.remove(espacio);
    }

    public Espacio buscarPorId(Long id) {
        return entityManager.find(Espacio.class, id);
    }

    public List<Espacio> listarTodos() {
        return entityManager.createQuery("SELECT e FROM Espacio e", Espacio.class).getResultList();
    }
}
