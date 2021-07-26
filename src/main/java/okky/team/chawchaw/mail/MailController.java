package okky.team.chawchaw.mail;

import lombok.RequiredArgsConstructor;
import okky.team.chawchaw.utils.dto.DefaultResponseVo;
import okky.team.chawchaw.utils.message.ResponseMailMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequiredArgsConstructor
public class MailController {

    private final MailService mailService;

    @PostMapping("users/email/send")
    public ResponseEntity sendMail(HttpServletRequest request, @RequestBody MailDto mailDto){
        String domain = mailDto.getEmail().split("@")[1];

        if (domain.length() > 5 && domain.substring(domain.length() - 6, domain.length()).equals(".ac.kr")){
            HttpSession session = request.getSession();
            mailService.sendMail(session, mailDto);
            return new ResponseEntity(DefaultResponseVo.res(ResponseMailMessage.SEND_SUCCESS, true), HttpStatus.OK);
        }
        else
            return new ResponseEntity(DefaultResponseVo.res(ResponseMailMessage.WRONG_EMAIL_DOMAIN, false), HttpStatus.OK);


    }

    @PostMapping("users/email/verification")
    public ResponseEntity verificationMail(HttpServletRequest request, @RequestBody MailDto mailDto){
        HttpSession session = request.getSession();
        Object expect = session.getAttribute(mailDto.getEmail());
        if (expect != null)
            if (Integer.parseInt(expect.toString()) == mailDto.getToken()) {
                session.removeAttribute(mailDto.getEmail());
                return new ResponseEntity(DefaultResponseVo.res(ResponseMailMessage.VERIFICATION_SUCCESS, true), HttpStatus.OK);
            }
            else {
                session.removeAttribute(mailDto.getEmail());
                return new ResponseEntity(DefaultResponseVo.res(ResponseMailMessage.VERIFICATION_FAIL, false), HttpStatus.OK);
            }

        return new ResponseEntity(DefaultResponseVo.res(ResponseMailMessage.EXPIRED_TOKEN, false), HttpStatus.OK);

    }
}
