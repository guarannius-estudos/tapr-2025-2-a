package br.univille.authservice.interfaces.rest;

import br.univille.authservice.application.user.ListUsersHandler;
import br.univille.authservice.application.user.RegisterUserHandler;
import br.univille.authservice.interfaces.rest.dto.user.RegisterUserRequest;
import br.univille.authservice.interfaces.rest.dto.user.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final ListUsersHandler listUsersHandler;
    private final RegisterUserHandler registerUserHandler;

    @GetMapping
    public ResponseEntity<Page<UserResponse>> list(Pageable pageable) {
        Page<UserResponse> page = listUsersHandler.handle(pageable);
        return ResponseEntity.ok(page);
    }

    @PostMapping
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterUserRequest request) {
        UserResponse created =registerUserHandler.handle(request.name(), request.email(), request.password());
        return ResponseEntity.created(URI.create("/users/" + created.id())).body(created);
    }
}
