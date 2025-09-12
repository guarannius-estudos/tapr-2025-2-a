package br.univille.authservice.domain.auth;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ExpiresAt {
    @Column(name = "expires_at", nullable = false)
    private Instant value;

    private ExpiresAt(Instant value) {
        this.value = value;
    }

    public static ExpiresAt at(Instant instant) {
        return new ExpiresAt(instant);
    }

    public boolean isAfter(Instant instant) {
        return value.isAfter(instant);
    }
}
