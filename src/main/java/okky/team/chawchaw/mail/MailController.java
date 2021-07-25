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
        HttpSession session = request.getSession();
        mailService.sendMail(session, mailDto);

        return new ResponseEntity(DefaultResponseVo.res(ResponseMailMessage.SEND_SUCCESS, true), HttpStatus.OK);
    }

    @PostMapping("users/email/verification")
    public ResponseEntity verificationMail(HttpServletRequest request, @RequestBody MailDto mailDto){
        HttpSession session = request.getSession();
        Object expect = session.getAttribute(mailDto.getEmail());
        if (expect != null)
            if (Integer.parseInt(expect.toString()) == mailDto.getToken())
                return new ResponseEntity(DefaultResponseVo.res(ResponseMailMessage.VERIFICATION_SUCEESS, true), HttpStatus.OK);
            else {
                session.invalidate();
            }

        return new ResponseEntity(DefaultResponseVo.res(ResponseMailMessage.VERIFICATION_FAIL, false), HttpStatus.OK);
    }
}
