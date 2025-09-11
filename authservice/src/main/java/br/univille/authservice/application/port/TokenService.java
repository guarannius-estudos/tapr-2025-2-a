package br.univille.authservice.application.port;

import br.univille.authservice.domain.user.User;

public interface TokenService {
    record TokenPair(
        String accessToken,
        String refreshToken,
        long expiresInSeconds
    ) {

    }

    TokenPair issue(User user);
}
