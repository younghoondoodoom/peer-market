package doodoom.project.peermarket.validation;

import doodoom.project.peermarket.dto.MemberRegisterInput;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class ValidationTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    public void emailValidation() throws Exception {
        //given
        MemberRegisterInput input = MemberRegisterInput.builder()
                .email("test")
                .password("test123!")
                .passwordCheck("test123!")
                .nickname("test")
                .build();

        //when
        Set<ConstraintViolation<MemberRegisterInput>> validation =
                validator.validate(input);

        //then
        assertThat(validation.size()).isEqualTo(1);
        assertThat(validation.iterator().next().getMessage()).isEqualTo("올바른 " +
                "형식의 이메일 주소여야 합니다.");
    }

    @Test
    public void passwordValidation() throws Exception {
        //given
        MemberRegisterInput input = MemberRegisterInput.builder()
                .email("test@test.com")
                .password("testtest")
                .passwordCheck("testtest")
                .nickname("test")
                .build();
        //when
        Set<ConstraintViolation<MemberRegisterInput>> validation =
                validator.validate(input);

        //then
        assertThat(validation.size()).isEqualTo(1);
        assertThat(validation.iterator().next().getMessage()).isEqualTo(
                "비밀번호는 영어, 숫자를 포함해 8~20 글자이어야 합니다.");
    }

    @Test
    public void passwordCheckValidation() throws Exception {
        //given
        MemberRegisterInput input = MemberRegisterInput.builder()
                .email("test@test.com")
                .password("test1234")
                .passwordCheck("test12345")
                .nickname("test")
                .build();
        //when
        Set<ConstraintViolation<MemberRegisterInput>> validation =
                validator.validate(input);

        //then
        assertThat(validation.size()).isEqualTo(1);
        assertThat(validation.iterator().next().getMessage()).isEqualTo(
                "비밀번호가 일치하지 않습니다.");
    }
}
