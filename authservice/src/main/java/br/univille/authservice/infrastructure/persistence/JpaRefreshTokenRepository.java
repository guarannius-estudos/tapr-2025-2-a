package br.univille.authservice.infrastructure.persistence;

import br.univille.authservice.domain.auth.RefreshToken;
import br.univille.authservice.domain.auth.RefreshTokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
class JpaRefreshTokenRepository implements RefreshTokenRepository {

    private final SpringDataRefreshTokenJpa jpa;

    @Override
    public RefreshToken save(RefreshToken token) {
        return jpa.save(token);
    }

    @Override
    public Optional<RefreshToken> findActiveByHash(String tokenHashBase64) {
        return jpa.findActiveByHash(tokenHashBase64, Instant.now());
    }

    @Override
    @Transactional
    public void revoke(UUID id) {
        jpa.revokeById(id, Instant.now());
    }

    @Override
    public void deleteById(UUID id) {
        jpa.deleteById(id);
    }
}
