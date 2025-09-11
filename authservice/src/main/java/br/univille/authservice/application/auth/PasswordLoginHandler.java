package br.univille.authservice.application.auth;

import br.univille.authservice.application.port.PasswordHasher;
import br.univille.authservice.application.port.TokenService;
import br.univille.authservice.domain.user.User;
import br.univille.authservice.domain.user.UserRepository;
import br.univille.authservice.domain.user.vo.Email;
import br.univille.authservice.interfaces.rest.dto.auth.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PasswordLoginHandler {
    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;
    private final TokenService tokenService;

    public TokenResponse handle(String rawEmail, String rawPassword) {
        Email email = Email.of(rawEmail);
        Optional<User> userOpt = userRepository.findByEmail(email.getValue());

        if (userOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciais estão inválidas");
        }

        User user = userOpt.get();

        if (!passwordHasher.matches(rawPassword, user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciais estão inválidas");
        }

        TokenService.TokenPair pair = tokenService.issue(user);
        return new TokenResponse(
                pair.accessToken(),
                pair.refreshToken(),
                pair.expiresInSeconds()
        );
    }
}
