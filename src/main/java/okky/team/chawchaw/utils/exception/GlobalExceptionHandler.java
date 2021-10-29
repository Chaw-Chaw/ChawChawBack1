package okky.team.chawchaw.utils.exception;

import com.mysql.cj.jdbc.exceptions.MysqlDataTruncation;
import lombok.extern.slf4j.Slf4j;
import okky.team.chawchaw.block.exception.BlockedUserException;
import okky.team.chawchaw.user.exception.ConnectElseWhereException;
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
    protected ResponseEntity<?> usernameNotFound(UsernameNotFoundException e) {
        return new ResponseEntity<>(DefaultResponseVo.res(ResponseGlobalMessage.G400), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MultipartException.class)
    protected ResponseEntity<?> multipartException(MultipartException e) {
        return new ResponseEntity<>(DefaultResponseVo.res(ResponseGlobalMessage.G404), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    protected ResponseEntity<?> sqlIntegrityConstrainViolation(SQLIntegrityConstraintViolationException e) {
        return new ResponseEntity<>(DefaultResponseVo.res(ResponseGlobalMessage.G500), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<?> illegalArgument(IllegalArgumentException e) {
        return new ResponseEntity<>(DefaultResponseVo.res(ResponseGlobalMessage.G500), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MysqlDataTruncation.class)
    protected ResponseEntity<?> mysqlDataTruncation(MysqlDataTruncation e) {
        return new ResponseEntity<>(DefaultResponseVo.res(ResponseGlobalMessage.G500), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(PointMyselfException.class)
    protected ResponseEntity<?> pointMyself(PointMyselfException e) {
        return new ResponseEntity<>(DefaultResponseVo.res(ResponseGlobalMessage.G401), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BlockedUserException.class)
    protected ResponseEntity<?> blockUserException(BlockedUserException e) {
        return new ResponseEntity<>(DefaultResponseVo.res(ResponseGlobalMessage.G402), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(BindException.class)
    protected ResponseEntity<?> bindException(BindException e) {
        return new ResponseEntity<>(DefaultResponseVo.res(ResponseGlobalMessage.G405), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConnectElseWhereException.class)
    protected ResponseEntity<?> connectElseWhereException(ConnectElseWhereException e) {
        return new ResponseEntity<>(DefaultResponseVo.res(ResponseGlobalMessage.G403), HttpStatus.UNAUTHORIZED);
    }

}
