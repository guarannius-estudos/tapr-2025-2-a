package br.univille.authservice.infrastructure.mail;

import br.univille.authservice.application.port.MailSender;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@Profile("prod")
@RequiredArgsConstructor
public class SmtpMailSender implements MailSender {
    private final JavaMailSender mailSender;

    @Override
    public void sendMagicLink(String toEmail, String magicUrl, Instant expiresAt) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");

            helper.setTo(toEmail);
            helper.setSubject("Seu link mágico de login");
            helper.setText(
                    "<p><a href=\"" + magicUrl + "\">Entrar</a></p>" +
                    "<p>Link expira em: " + expiresAt + "</p>",
                    true
            );

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Erro ao enviar e-mail", e);
        }
    }
}
