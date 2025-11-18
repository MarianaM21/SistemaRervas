package com.sistema_reservas.repository;

import com.sistema_reservas.model.RefreshToken;
import com.sistema_reservas.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    Optional<RefreshToken> findByUsuario(Usuario usuario);

    void deleteByUsuario(Usuario usuario);
}
