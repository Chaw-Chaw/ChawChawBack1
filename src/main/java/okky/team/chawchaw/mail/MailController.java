package okky.team.chawchaw.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class MailController {

    private final MailService mailService;

    @PostMapping("users/send-mail")
    public ResponseEntity<String> sendMail(HttpServletRequest request, @RequestBody MailDto mailDto){
        HttpSession session = request.getSession();
        mailService.sendMail(session, mailDto);
        return ResponseEntity.status(HttpStatus.OK).body("");
    }

    @PostMapping("users/verification-mail")
    public ResponseEntity<Boolean> verificationMail(HttpServletRequest request, @RequestBody MailDto mailDto){
        HttpSession session = request.getSession();
        Object expect = session.getAttribute(mailDto.getEmail());
        if (expect != null)
            if (Integer.parseInt(expect.toString()) == mailDto.getToken())
                return ResponseEntity.status(HttpStatus.OK).body(true);
            else {
                session.invalidate();
            }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
    }
}
