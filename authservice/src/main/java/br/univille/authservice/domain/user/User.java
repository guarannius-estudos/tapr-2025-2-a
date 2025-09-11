package br.univille.authservice.domain.user;

import br.univille.authservice.domain.user.vo.Email;
import br.univille.authservice.domain.user.vo.Role;
import br.univille.authservice.domain.user.vo.RoleType;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Table(name = "user_table")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class User {
    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Valid
    @Embedded
    private Email email;

    @Embedded
    private Role role;

    public User(String name, @Valid Email email, RoleType role, String password) {
        this.name = name;
        this.email = email;
        this.role = Role.of(role);
        this.password = password;
    }
}
