package br.univille.authservice.interfaces.rest;

import br.univille.authservice.application.auth.PasswordLoginHandler;
import br.univille.authservice.application.auth.RefreshTokenService;
import br.univille.authservice.interfaces.rest.dto.auth.LogoutRequest;
import br.univille.authservice.interfaces.rest.dto.auth.PasswordLoginRequest;
import br.univille.authservice.interfaces.rest.dto.auth.RefreshRequest;
import br.univille.authservice.interfaces.rest.dto.auth.TokenResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "Auth")
public class AuthController {
    private final PasswordLoginHandler passwordLoginHandler;
    private final RefreshTokenService refreshService;

    @PostMapping("/login/password")
    public ResponseEntity<TokenResponse> loginWithPassword(@Valid @RequestBody PasswordLoginRequest request) {
        TokenResponse response = passwordLoginHandler.handle(request.email(), request.password());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(@Valid @RequestBody RefreshRequest body) {
        return ResponseEntity.ok(refreshService.refresh(body.refreshToken()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@Valid @RequestBody LogoutRequest body) {
        refreshService.logout(body.refreshToken());
        return ResponseEntity.noContent().build();
    }
}
