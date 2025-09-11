package br.univille.authservice.application.user;

import br.univille.authservice.domain.user.User;
import br.univille.authservice.domain.user.UserRepository;
import br.univille.authservice.interfaces.rest.dto.user.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ListUsersHandler {
    private final UserRepository userRepository;

    public ListUsersHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Page<UserResponse> handle(Pageable pageable) {
        Page<User> page = userRepository.findAll(pageable);
        return page.map(user -> new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail().getValue(),
                user.getRole().getValue().name()
        ));
    }
}
