package okky.team.chawchaw.mail;

import lombok.RequiredArgsConstructor;
import okky.team.chawchaw.mail.dto.MailDto;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;

    public void sendMail(HttpSession session, MailDto mailDto){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailDto.getEmail());
        message.setSubject("[ChawChaw] 이메일 인증번호가 도착하였습니다.");
        String token = String.valueOf((int)(Math.random() * 89999) + 10000);
        message.setText("" +
                "이메일 인증을 완료하려면 아래의 인증번호를 입력해주세요.\n" +
                "인증번호 : [" + token +"]\n" +
                "\n" +
                "* 개인정보 보호를 위해 이메일 인증번호는 3분간 유효합니다.\n" +
                "\n" +
                "본 메일은 발신전용 메일입니다. 문의는 고객센터를 이용해 주세요.\n" +
                "\n" +
                "대표 : 노두현, 하주현, 김진현\n" +
                "Copyright (c) . ChawChaw All rights reserved.");
        session.setAttribute(mailDto.getEmail(), token);
        javaMailSender.send(message);
    }

}
