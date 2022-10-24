package doodoom.project.peermarket.service.member;

import doodoom.project.peermarket.domain.Member;
import doodoom.project.peermarket.dto.MemberRegisterInput;
import doodoom.project.peermarket.exception.MemberException;
import doodoom.project.peermarket.repository.MemberRepository;
import doodoom.project.peermarket.type.MemberStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static doodoom.project.peermarket.type.ErrorCode.EMAIL_ALREADY_EXIST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private MemberServiceImpl memberService;
    private static final List<Member> members = new ArrayList<>();

    @BeforeAll
    public static void setUp() {
        members.add(Member.builder()
                .email("test@test.com")
                .password("test123!")
                .status(MemberStatus.ACTIVE)
                .build());
    }

    @Test
    public void registerSuccess() {
        //given
        Member member = members.get(0);
        MemberRegisterInput input =
                new MemberRegisterInput(member.getEmail(), member.getPassword(),
                        member.getPassword(), member.getNickname());

        given(memberRepository.findByEmail(anyString()))
                .willReturn(Optional.empty());
        given(passwordEncoder.encode(anyString()))
                .willReturn(anyString());

        ArgumentCaptor<Member> captor = ArgumentCaptor.forClass(Member.class);

        //when
        boolean b = memberService.register(input);

        //then
        assertThat(b).isTrue();
        verify(memberRepository, times(1)).save(captor.capture());
    }

    @Test
    public void registerFailure() {
        //given
        Member member = members.get(0);
        MemberRegisterInput input =
                new MemberRegisterInput(member.getEmail(), member.getPassword(),
                        member.getPassword(), member.getNickname());

        //when
        given(memberRepository.findByEmail(anyString()))
                .willReturn(Optional.of(member));

        //then
        MemberException exception = assertThrows(MemberException.class,
                () -> memberService.register(input));
        assertThat(exception.getErrorCode()).isEqualTo(EMAIL_ALREADY_EXIST);
        assertThat(exception.getErrorMessage()).isEqualTo(EMAIL_ALREADY_EXIST.getDescription());
    }
}