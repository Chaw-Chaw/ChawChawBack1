package okky.team.chawchaw.utils.exception;

import lombok.extern.slf4j.Slf4j;
import okky.team.chawchaw.utils.dto.DefaultResponseVo;
import okky.team.chawchaw.utils.message.ResponseDataMessage;
import okky.team.chawchaw.utils.message.ResponseUserMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLIntegrityConstraintViolationException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(UsernameNotFoundException.class)
    protected ResponseEntity usernameNotFound(UsernameNotFoundException e) {
        log.warn("해당 아이디를 찾을 수 없음", e.getMessage());
        return new ResponseEntity(DefaultResponseVo.res(ResponseUserMessage.ID_NOT_EXIST, false), HttpStatus.OK);
    }

    @ExceptionHandler(DuplicationUserEmailException.class)
    protected ResponseEntity duplicationUserEmail(DuplicationUserEmailException e) {
        return new ResponseEntity(DefaultResponseVo.res(ResponseUserMessage.EMAIL_EXIST, true), HttpStatus.OK);
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    protected ResponseEntity sqlIntegrityConstrainViolation(SQLIntegrityConstraintViolationException e) {
        log.error("무결성 제약 조건 위배", e.getMessage());
        return new ResponseEntity(DefaultResponseVo.res(ResponseDataMessage.SQL_ERROR, false), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity illegalArgument(IllegalArgumentException e) {
        log.error("페이지 넘버 잘못된 요청", e.getMessage());
        return new ResponseEntity(DefaultResponseVo.res(ResponseDataMessage.SQL_ERROR, false), HttpStatus.BAD_REQUEST);
    }


}
