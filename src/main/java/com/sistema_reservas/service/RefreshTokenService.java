package com.sistema_reservas.service;

import com.sistema_reservas.model.RefreshToken;
import com.sistema_reservas.model.Usuario;
import com.sistema_reservas.repository.RefreshTokenRepository;
import com.sistema_reservas.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    // Crea o actualiza un refresh token para un usuario.

    @Transactional
    public RefreshToken crearRefreshToken(Usuario usuario) {
        Usuario managedUsuario = userRepository.findById(usuario.getId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Buscar token existente
        Optional<RefreshToken> existingTokenOpt = refreshTokenRepository.findByUsuario(managedUsuario);

        RefreshToken token;
        if (existingTokenOpt.isPresent()) {
            token = existingTokenOpt.get();
        } else {
            token = new RefreshToken();
            token.setUsuario(managedUsuario);
        }

        token.setToken(UUID.randomUUID().toString());
        token.setExpiracion(Instant.now().plusSeconds(60 * 60 * 24 * 7)); // 7 días
        token.setRevocado(false);

        return refreshTokenRepository.save(token);
    }

    //Valida un refresh token existente.
    public RefreshToken validarRefreshToken(String token) {
        RefreshToken refresh = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Refresh token no existe"));

        if (refresh.isRevocado()) {
            throw new RuntimeException("Refresh token revocado");
        }

        if (refresh.getExpiracion().isBefore(Instant.now())) {
            throw new RuntimeException("Refresh token expirado");
        }

        return refresh;
    }

    //Revoca un refresh token

    @Transactional
    public void revocarRefreshToken(String token) {
        RefreshToken refresh = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Refresh token no existe"));

        refresh.setRevocado(true);
        refreshTokenRepository.save(refresh);
    }

    @Transactional
    public void eliminarTokensDeUsuario(Long usuarioId) {
        Usuario usuario = userRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        refreshTokenRepository.findByUsuario(usuario)
                .ifPresent(refreshTokenRepository::delete);
    }

}

