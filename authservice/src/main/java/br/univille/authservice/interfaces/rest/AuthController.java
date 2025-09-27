package br.univille.authservice.interfaces.rest;

import br.univille.authservice.application.auth.PasswordLoginHandler;
import br.univille.authservice.application.auth.RefreshTokenHandler;
import br.univille.authservice.application.auth.RequestMagicLinkHandler;
import br.univille.authservice.application.auth.VerifyMagicLinkHandler;
import br.univille.authservice.interfaces.rest.dto.auth.LogoutRequest;
import br.univille.authservice.interfaces.rest.dto.auth.MagicLinkRequest;
import br.univille.authservice.interfaces.rest.dto.auth.MagicLinkVerifyRequest;
import br.univille.authservice.interfaces.rest.dto.auth.PasswordLoginRequest;
import br.univille.authservice.interfaces.rest.dto.auth.RefreshRequest;
import br.univille.authservice.interfaces.rest.dto.auth.TokenResponse;
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
    private final RefreshTokenHandler refreshService;
    private final RequestMagicLinkHandler requestMagicLinkHandler;
    private final VerifyMagicLinkHandler verifyMagicLinkHandler;

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

    @PostMapping("/login/magic")
    public ResponseEntity<Void> requestMagic(@Valid @RequestBody MagicLinkRequest req) {
        requestMagicLinkHandler.handle(req.email());
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/login/magic/verify")
    public ResponseEntity<TokenResponse> verifyMagic(@Valid @RequestBody MagicLinkVerifyRequest req) {
        TokenResponse tokens = verifyMagicLinkHandler.handle(req.token());
        return ResponseEntity.ok(tokens);
    }
}
