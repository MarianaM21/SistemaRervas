package com.sistema_reservas_copia.repository;

import com.sistema_reservas_copia.model.usuarios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<usuarios, Long> {
    boolean existsByEmail(String email);
    Optional<usuarios> findByEmail(String email);
}

