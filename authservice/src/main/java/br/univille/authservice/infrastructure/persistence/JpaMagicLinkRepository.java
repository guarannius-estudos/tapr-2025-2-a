package br.univille.authservice.infrastructure.persistence;

import br.univille.authservice.domain.auth.MagicLink;
import br.univille.authservice.domain.auth.MagicLinkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class JpaMagicLinkRepository implements MagicLinkRepository {
    private final SpringDataMagicLinkJpa jpa;

    @Override
    public MagicLink save(MagicLink magicLink) {
        return jpa.save(magicLink);
    }

    @Override
    public Optional<MagicLink> findValidByHash(String hash, Instant now) {
        return jpa.findByHashedTokenValueAndConsumedAtIsNullAndExpiresAtValueGreaterThan(hash, now);
    }

    @Override
    public Optional<MagicLink> findById(UUID id) {
        return jpa.findById(id);
    }
}
