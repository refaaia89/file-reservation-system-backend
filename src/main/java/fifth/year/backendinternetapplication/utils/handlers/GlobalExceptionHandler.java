package fifth.year.backendinternetapplication.utils.handlers;

import fifth.year.backendinternetapplication.dto.response.main.ErrorResponse;
import fifth.year.backendinternetapplication.utils.exceptions.ObjectNotValidException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> handleException(ResponseStatusException exception){
        return ResponseEntity.status(exception.getStatusCode())
                .body(new ErrorResponse(
                exception.getReason(), null
        ));
    }

    @ExceptionHandler(ObjectNotValidException.class)
    public ResponseEntity<ErrorResponse> handleException(ObjectNotValidException exception) {
        return ResponseEntity.badRequest().body(new ErrorResponse(
                exception.getMessage(), exception.getErrorMessages()
        ));
    }

    @ExceptionHandler({ AuthenticationException.class })
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(ex.getMessage(),null));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        return ResponseEntity.internalServerError().body(new ErrorResponse(
                exception.getMessage(), null
        ));
    }
}
