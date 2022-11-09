package doodoom.project.peermarket.service.member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import doodoom.project.peermarket.domain.Member;
import doodoom.project.peermarket.dto.MemberRegisterInput;
import doodoom.project.peermarket.exception.member.EmailAlreadyExistException;
import doodoom.project.peermarket.exception.member.MemberStatusStopException;
import doodoom.project.peermarket.repository.member.MemberRepository;
import doodoom.project.peermarket.type.MemberStatus;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

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
        assertThrows(EmailAlreadyExistException.class,
            () -> memberService.register(input));
    }

    @Test
    public void loadUserByUsernameFailureUsernameNotFound() {
        //given
        given(memberRepository.findByEmail(anyString()))
            .willReturn(Optional.empty());

        //when
        //then
        assertThrows(UsernameNotFoundException.class,
            () -> memberService.loadUserByUsername(anyString()));
    }

    @Test
    public void loadUserByUsernameFailureMemberStopped() {
        //given
        Member stoppedMember = Member.builder()
            .id(1L)
            .email("test@test.com")
            .password("test1234")
            .nickname("test")
            .isAdmin(false)
            .status(MemberStatus.STOP)
            .build();

        given(memberRepository.findByEmail(anyString()))
            .willReturn(Optional.of(stoppedMember));

        //when
        //then
        assertThrows(MemberStatusStopException.class,
            () -> memberService.loadUserByUsername(anyString()));
    }

    @Test
    public void loadUserByUsernameSuccessAdmin() {
        //given
        Member adminMember = Member.builder()
            .id(1L)
            .email("test@test.com")
            .password("test1234")
            .nickname("test")
            .isAdmin(true)
            .status(MemberStatus.ACTIVE)
            .build();

        given(memberRepository.findByEmail(anyString()))
            .willReturn(Optional.of(adminMember));

        //when
        UserDetails user =
            memberService.loadUserByUsername(adminMember.getEmail());

        //then
        assertThat(user.getUsername()).isEqualTo(adminMember.getEmail());
        assertThat(user.getPassword()).isEqualTo(adminMember.getPassword());
        assertThat(user.getAuthorities().size()).isEqualTo(2);

        List<? extends GrantedAuthority> authorities =
            new ArrayList<>(user.getAuthorities());
        assertThat(authorities.get(0).getAuthority()).isEqualTo("ROLE_ADMIN");
        assertThat(authorities.get(1).getAuthority()).isEqualTo("ROLE_USER");
    }

    @Test
    public void loadUserByUsernameSuccess() {
        //given
        Member member = Member.builder()
            .id(1L)
            .email("test@test.com")
            .password("test1234")
            .nickname("test")
            .isAdmin(false)
            .status(MemberStatus.ACTIVE)
            .build();

        given(memberRepository.findByEmail(anyString()))
            .willReturn(Optional.of(member));

        //when
        UserDetails user =
            memberService.loadUserByUsername(member.getEmail());

        //then
        assertThat(user.getUsername()).isEqualTo(member.getEmail());
        assertThat(user.getPassword()).isEqualTo(member.getPassword());
        assertThat(user.getAuthorities().size()).isEqualTo(1);
        List<? extends GrantedAuthority> authorities =
            new ArrayList<>(user.getAuthorities());
        assertThat(authorities.get(0).getAuthority()).isEqualTo("ROLE_USER");
    }
}