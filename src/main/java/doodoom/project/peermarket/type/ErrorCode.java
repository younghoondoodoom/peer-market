package doodoom.project.peermarket.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    EMAIL_ALREADY_EXIST("이메일이 중복 되었습니다."),
    INTERNAL_SERVER_ERROR("서버 에러가 발생했습니다."),
    INPUT_FIELD_ERROR("입력 값이 올바르지 않습니다.");
    private final String description;
}
