package br.univille.authservice.infrastructure.persistence;

import br.univille.authservice.domain.auth.MagicLink;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface SpringDataMagicLinkJpa extends JpaRepository<MagicLink, UUID> {
    Optional<MagicLink> findByHashedTokenValueAndConsumedAtIsNullAndExpiresAtValueGreaterThan(String tokenHash, Instant now);
}
