package br.univille.authservice.domain.auth;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository {
    RefreshToken save(RefreshToken token);
    Optional<RefreshToken> findActiveByHash(String tokenHashBase64);
    void revoke(UUID id);
    void deleteById(UUID id);
}
