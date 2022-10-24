package doodoom.project.peermarket.exception;

import doodoom.project.peermarket.dto.ErrorResponse;
import doodoom.project.peermarket.type.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

import static doodoom.project.peermarket.type.ErrorCode.INPUT_FIELD_ERROR;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MemberException.class)
    public ResponseEntity<?> handleMemberException(MemberException e) {
        log.warn("{} is occurred", e.getErrorCode());
        ErrorResponse response = ErrorResponse.builder()
                .errorCode(e.getErrorCode())
                .errorMessage(e.getErrorMessage())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<?> handleBindException(BindException e) {
        log.warn("{} is occurred", INPUT_FIELD_ERROR);
        List<ErrorResponse> responses = new ArrayList<>();
        for (FieldError error : e.getFieldErrors()) {
            responses.add(ErrorResponse.builder()
                    .errorCode(INPUT_FIELD_ERROR)
                    .errorMessage(error.getDefaultMessage())
                    .build());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responses);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) {
        log.error("{} is occurred", ErrorCode.INTERNAL_SERVER_ERROR);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
    }
}
