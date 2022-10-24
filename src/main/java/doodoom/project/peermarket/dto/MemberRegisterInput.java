package doodoom.project.peermarket.dto;

import lombok.*;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberRegisterInput {

    @Email(message = "올바른 형식의 이메일 주소여야 합니다.")
    private String email;
    @NotEmpty(message = "비밀번호는 필수 입력 사항입니다.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d~!@#$%^&*()+|=]{8," +
            "20}$", message = "비밀번호는 영어, 숫자를 포함해 8~20 글자이어야 합니다.")
    private String password;
    @NotEmpty(message = "비밀번호 확인은 필수 입력 사항입니다.")
    private String passwordCheck;
    @NotEmpty(message = "닉네임은 필수 입력 사항입니다.")
    private String nickname;

    @AssertTrue(message = "비밀번호가 일치하지 않습니다.")
    boolean isEqualPassword() {
        return this.password.equals(this.passwordCheck);
    }
}
