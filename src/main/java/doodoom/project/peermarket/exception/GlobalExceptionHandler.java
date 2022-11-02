package doodoom.project.peermarket.exception;

import doodoom.project.peermarket.dto.ErrorResponse;
import doodoom.project.peermarket.exception.file.FileLoadException;
import doodoom.project.peermarket.exception.member.EmailAlreadyExistException;
import doodoom.project.peermarket.exception.member.MemberNotFoundException;
import doodoom.project.peermarket.exception.member.MemberStatusStopException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({EmailAlreadyExistException.class, MemberStatusStopException.class, MemberNotFoundException.class})
    public String handleMemberException(RuntimeException e, Model model) {
        log.info("MemberException is occurred");
        ErrorResponse errorResponse = ErrorResponse.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .message(e.getMessage())
                .build();
        model.addAttribute("errorResponse", errorResponse);
        return "error/memberError";
    }

    @ExceptionHandler(BindException.class)
    public String handleBindException(BindException e, Model model) {
        log.info("BindException is occurred");
        List<ErrorResponse> errorResponses = new ArrayList<>();
        for (FieldError error : e.getFieldErrors()) {
            errorResponses.add(ErrorResponse.builder()
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .message(error.getDefaultMessage())
                    .build());
        }
        model.addAttribute("errorResponses", errorResponses);
        return "error/validation";
    }

    @ExceptionHandler(FileLoadException.class)
    public String handleFileLoadException(FileLoadException e, Model model) {
        log.info("FileLoadException is occurred");
        ErrorResponse errorResponse = ErrorResponse.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .message(e.getMessage())
                .build();
        model.addAttribute("errorResponse", errorResponse);
        return "error/fileLoadError";
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Exception e) {
        log.error(e.getMessage());
        return "error/500";
    }
}
