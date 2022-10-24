package doodoom.project.peermarket.exception;

import doodoom.project.peermarket.type.ErrorCode;
import lombok.Getter;

@Getter
public class MemberException extends RuntimeException {
    private final ErrorCode errorCode;
    private final String errorMessage;

    public MemberException(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDescription();
    }
}
