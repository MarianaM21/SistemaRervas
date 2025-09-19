package com.sistema_reservas.repository;

import com.sistema_reservas.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<Usuario, Long> {
    boolean existsByEmail(String email);
}
