package okky.team.chawchaw.mail;

import lombok.RequiredArgsConstructor;
import okky.team.chawchaw.mail.dto.MailDto;
import okky.team.chawchaw.mail.dto.MailVerificationDto;
import okky.team.chawchaw.utils.dto.DefaultResponseVo;
import okky.team.chawchaw.utils.message.ResponseGlobalMessage;
import okky.team.chawchaw.utils.message.ResponseMailMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class MailController {

    private final MailService mailService;

    @PostMapping("mail/send")
    public ResponseEntity<?> sendMail(HttpServletRequest request, @Valid @RequestBody MailDto mailDto){
        String domain = mailDto.getEmail().split("@")[1];

        if (domain.length() > 5 && domain.startsWith(".ac.kr", domain.length() - 6)){
            HttpSession session = request.getSession();
            mailService.sendMail(session, mailDto);
            return new ResponseEntity<>(DefaultResponseVo.res(ResponseGlobalMessage.G200), HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(DefaultResponseVo.res(ResponseMailMessage.E400), HttpStatus.BAD_REQUEST);


    }

    @PostMapping("mail/verification")
    public ResponseEntity<?> verificationMail(HttpServletRequest request, @Valid @RequestBody MailVerificationDto mailVerificationDto){
        HttpSession session = request.getSession();
        Object expect = session.getAttribute(mailVerificationDto.getEmail());
        if (expect != null)
            if (Integer.parseInt(expect.toString()) == mailVerificationDto.getVerificationNumber()) {
                session.removeAttribute(mailVerificationDto.getEmail());
                return new ResponseEntity<>(DefaultResponseVo.res(ResponseGlobalMessage.G200), HttpStatus.OK);
            }
            else {
                session.removeAttribute(mailVerificationDto.getEmail());
                return new ResponseEntity<>(DefaultResponseVo.res(ResponseMailMessage.E401), HttpStatus.BAD_REQUEST);
            }

        return new ResponseEntity<>(DefaultResponseVo.res(ResponseMailMessage.E402), HttpStatus.BAD_REQUEST);

    }
}
