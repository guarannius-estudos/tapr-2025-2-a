package br.univille.authservice.domain.auth;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class TokenHash {
    @Column(name = "token_hash", nullable = false, length = 88, unique = true)
    private String value;

    private TokenHash(String value) {
        this.value = value;
    }

    public static TokenHash fromHashed(String hash) {
        return new TokenHash(hash);
    }

    /** Hash a raw refresh token string using SHA-256 + Base64URL (no padding) */
    public static TokenHash ofPlainText(String raw) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(raw.getBytes(StandardCharsets.UTF_8));
            String b64 = Base64.getUrlEncoder().withoutPadding().encodeToString(digest);
            return new TokenHash(b64);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 not available", e);
        }
    }
}
