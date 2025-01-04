package marketplace.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.Timestamp;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorDetails> handleNonexistentProductArticleException(ApplicationException ex) {
        ErrorDetails errorDetails = ErrorDetails.builder()
                .exceptionName(ex.getClass().getSimpleName())
                .message(ex.getMessage())
                .timestamp(new Timestamp(System.currentTimeMillis()))
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDetails);
    }
}
