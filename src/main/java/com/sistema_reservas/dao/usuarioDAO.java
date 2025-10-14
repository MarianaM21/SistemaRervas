package com.sistema_reservas.dao;


import jakarta.persistence.EntityManager;
import com.sistema_reservas.model.Usuario;
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
    public Usuario buscarPorId(Long id) {
        return entityManager.find(Usuario.class, id);
    }

    // Buscar usuario por correo
    public Usuario buscarPorCorreo(String email) {
        try {
            String jpql = "SELECT u FROM Usuario u WHERE u.email = :email";
            return entityManager.createQuery(jpql, Usuario.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    // Listar todos los usuarios
    public List<Usuario> listarTodos() {
        String jpql = "SELECT u FROM Usuario u";
        return entityManager.createQuery(jpql, Usuario.class).getResultList();
    }

    // Guardar usuario
    public void guardar(Usuario usuario) {
        entityManager.persist(usuario);
    }

    // Actualizar usuario
    public Usuario actualizar(Usuario usuario) {
        return entityManager.merge(usuario);
    }

    // Eliminar usuario
    public void eliminar(Long id) {
        Usuario usuario = entityManager.find(Usuario.class, id);
        if (usuario != null) {
            entityManager.remove(usuario);
        }
    }

}