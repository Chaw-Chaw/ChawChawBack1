package okky.team.chawchaw.utils.exception;

import com.mysql.cj.jdbc.exceptions.MysqlDataTruncation;
import lombok.extern.slf4j.Slf4j;
import okky.team.chawchaw.block.exception.BlockedUserException;
import okky.team.chawchaw.user.exception.ConnectElseWhereException;
import okky.team.chawchaw.user.exception.DuplicationUserEmailException;
import okky.team.chawchaw.user.exception.PointMyselfException;
import okky.team.chawchaw.utils.dto.DefaultResponseVo;
import okky.team.chawchaw.utils.message.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;

import java.sql.SQLIntegrityConstraintViolationException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(UsernameNotFoundException.class)
    protected ResponseEntity usernameNotFound(UsernameNotFoundException e) {
        log.warn("해당 아이디를 찾을 수 없음", e.getMessage());
        return new ResponseEntity(DefaultResponseVo.res(ResponseGlobalMessage.G400), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MultipartException.class)
    protected ResponseEntity multipartException(MultipartException e) {
        log.warn("파일 타입이 잘못 됨", e);
        return new ResponseEntity(DefaultResponseVo.res(ResponseGlobalMessage.G405), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    protected ResponseEntity sqlIntegrityConstrainViolation(SQLIntegrityConstraintViolationException e) {
        log.error("무결성 제약 조건 위배", e);
        return new ResponseEntity(DefaultResponseVo.res(ResponseGlobalMessage.G402), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity illegalArgument(IllegalArgumentException e) {
        log.error("DB 잘못된 파라미터", e);
        return new ResponseEntity(DefaultResponseVo.res(ResponseGlobalMessage.G402), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MysqlDataTruncation.class)
    protected ResponseEntity mysqlDataTruncation(MysqlDataTruncation e) {
        return new ResponseEntity(DefaultResponseVo.res(ResponseGlobalMessage.G402), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PointMyselfException.class)
    protected ResponseEntity pointMyself(PointMyselfException e) {
        log.warn("자기 자신 선택");
        return new ResponseEntity(DefaultResponseVo.res(ResponseGlobalMessage.G401), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BlockedUserException.class)
    protected ResponseEntity blockUserException(BlockedUserException e) {
        return new ResponseEntity(DefaultResponseVo.res(ResponseGlobalMessage.G403), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(BindException.class)
    protected ResponseEntity bindException(BindException e) {
        return new ResponseEntity(DefaultResponseVo.res(ResponseGlobalMessage.G406), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConnectElseWhereException.class)
    protected ResponseEntity connectElseWhereException(ConnectElseWhereException e) {
        return new ResponseEntity(DefaultResponseVo.res(ResponseGlobalMessage.G404), HttpStatus.BAD_REQUEST);
    }

}
