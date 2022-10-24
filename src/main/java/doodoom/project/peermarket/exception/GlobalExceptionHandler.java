package doodoom.project.peermarket.exception;

import doodoom.project.peermarket.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
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
    public String handleMemberException(MemberException e, Model model) {
        log.warn("{} is occurred", e.getErrorCode());
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode(e.getErrorCode())
                .errorMessage(e.getErrorMessage())
                .build();
        model.addAttribute("errorResponse", errorResponse);
        return "error/400";
    }

    @ExceptionHandler(BindException.class)
    public String handleBindException(BindException e, Model model) {
        log.warn("{} is occurred", INPUT_FIELD_ERROR);
        List<ErrorResponse> errorResponses = new ArrayList<>();
        for (FieldError error : e.getFieldErrors()) {
            errorResponses.add(ErrorResponse.builder()
                    .errorCode(INPUT_FIELD_ERROR)
                    .errorMessage(error.getDefaultMessage())
                    .build());
        }
        model.addAttribute("errorResponses", errorResponses);
        return "error/validation";
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Exception e) {
        log.error("{} is occurred", e.getMessage());
        return "error/500";
    }
}
