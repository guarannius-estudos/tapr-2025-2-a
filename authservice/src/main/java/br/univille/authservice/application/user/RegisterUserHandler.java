package br.univille.authservice.application.user;

import br.univille.authservice.application.port.PasswordHasher;
import br.univille.authservice.domain.user.User;
import br.univille.authservice.domain.user.UserRepository;
import br.univille.authservice.domain.user.vo.Email;
import br.univille.authservice.domain.user.vo.RoleType;
import br.univille.authservice.interfaces.rest.dto.user.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class RegisterUserHandler {
    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;

    public UserResponse handle(String name, String emailRaw, String passwordRaw) {
        Email email = Email.of(emailRaw);

        if (userRepository.existsByEmail(email.getValue())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email já cadastrado");
        }

        String hashedPassword = passwordHasher.hash(passwordRaw);
        User user = new User(name, email, RoleType.CUSTOMER, hashedPassword);
        User savedUser = userRepository.save(user);
        return new UserResponse(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail().getValue(),
                savedUser.getRole().getValue().name()
        );
    }
}
