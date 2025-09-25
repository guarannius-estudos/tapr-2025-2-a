package br.univille.authservice.application.auth;

import br.univille.authservice.application.port.MailSender;
import br.univille.authservice.domain.auth.MagicLink;
import br.univille.authservice.domain.auth.MagicLinkRepository;
import br.univille.authservice.domain.auth.vo.ExpiresAt;
import br.univille.authservice.domain.auth.vo.HashedToken;
import br.univille.authservice.domain.user.User;
import br.univille.authservice.domain.user.UserRepository;
import br.univille.authservice.domain.user.vo.Email;
import br.univille.authservice.infrastructure.config.AppProperties;
import br.univille.authservice.support.Digests;
import br.univille.authservice.support.RandomTokenGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RequestMagicLinkHandler {
    private final UserRepository userRepository;
    private final AppProperties appProperties;
    private final MagicLinkRepository magicLinkRepository;
    private final MailSender mailSender;

    public record Result(boolean accepted) {}

    public Result handle(String emailRaw) {
        Email email = Email.of(emailRaw);

        Optional<User> userOpt = userRepository.findByEmail(emailRaw);
        if (userOpt.isEmpty()) {
            return new Result(true);
        }

        User user = userOpt.get();

        String token = RandomTokenGenerator.urlSafeToken(32);
        String hash = Digests.sha256Hex(token);

        Instant now = Instant.now();
        long ttl = appProperties.getMagicLink().getTtlSeconds();
        Instant exp = now.plusSeconds(ttl);

        MagicLink link = MagicLink.issueForLogin(
            user.getId(),
            HashedToken.of(hash),
            ExpiresAt.of(exp)
        );
        magicLinkRepository.save(link);

        String base = appProperties.getMagicLink().getVerifyUrlBase();
        String url = base + "?token=" + URLEncoder.encode(token, StandardCharsets.UTF_8);

        mailSender.sendMagicLink(
            email.getValue(),
            url,
            exp
        );

        return new Result(true);
    }
}
