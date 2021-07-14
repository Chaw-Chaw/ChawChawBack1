package okky.team.chawchaw.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;

    public void sendMail(HttpSession session, MailDto mailDto){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailDto.getEmail());
        message.setSubject("CHAW CHAW 인증 메일");
        String token = String.valueOf((int)(Math.random() * 8999) + 1000);
        message.setText(token);
        session.setAttribute(mailDto.getEmail(), token);
        session.setMaxInactiveInterval(60);
        javaMailSender.send(message);
    }

}
