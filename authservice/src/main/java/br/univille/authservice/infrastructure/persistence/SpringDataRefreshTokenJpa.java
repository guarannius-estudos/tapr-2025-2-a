package br.univille.authservice.infrastructure.persistence;

import br.univille.authservice.domain.auth.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

interface SpringDataRefreshTokenJpa extends JpaRepository<RefreshToken, UUID> {

    @Query("select t from RefreshToken t where t.tokenHash.value = :hash and t.revokedAt is null and t.expiresAt.value > :now")
    Optional<RefreshToken> findActiveByHash(String hash, Instant now);

    @Modifying
    @Query("update RefreshToken t set t.revokedAt = :now where t.id = :id and t.revokedAt is null")
    void revokeById(UUID id, Instant now);
}
