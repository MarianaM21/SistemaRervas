package com.sistema_reservas_copia.dao;


import jakarta.persistence.EntityManager;
import com.sistema_reservas_copia.model.usuarios;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public class usuarioDAO {

    @PersistenceContext
    private EntityManager entityManager;

    // Buscar usuario por ID
    public usuarios buscarPorId(Long id) {
        return entityManager.find(usuarios.class, id);
    }

    // Buscar usuario por correo
    public usuarios buscarPorCorreo(String email) {
        String jpql = "SELECT u FROM Usuario u WHERE u.email = :email";
        return entityManager.createQuery(jpql, usuarios.class)
                .setParameter("email", email)
                .getSingleResult();
    }

    // Listar todos los usuarios
    public List<usuarios> listarTodos() {
        String jpql = "SELECT u FROM Usuario u";
        return entityManager.createQuery(jpql, usuarios.class).getResultList();
    }

    // Guardar usuario
    public void guardar(usuarios usuario) {
        entityManager.persist(usuario);
    }

    // Actualizar usuario
    public usuarios actualizar(usuarios usuario) {
        return entityManager.merge(usuario);
    }

    // Eliminar usuario
    public void eliminar(Long id) {
        usuarios usuario = entityManager.find(usuarios.class, id);
        if (usuario != null) {
            entityManager.remove(usuario);
        }
    }
}