package doodoom.project.peermarket.service.member;

import static doodoom.project.peermarket.type.MemberStatus.ACTIVE;
import static doodoom.project.peermarket.type.MemberStatus.STOP;

import doodoom.project.peermarket.domain.Member;
import doodoom.project.peermarket.dto.MemberRegisterInput;
import doodoom.project.peermarket.exception.member.EmailAlreadyExistException;
import doodoom.project.peermarket.exception.member.MemberStatusStopException;
import doodoom.project.peermarket.repository.member.MemberRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public boolean register(MemberRegisterInput input) {
        if (memberRepository.findByEmail(input.getEmail()).isPresent()) {
            throw new EmailAlreadyExistException();
        }
        Member member = Member.builder()
            .email(input.getEmail())
            .password(passwordEncoder.encode(input.getPassword()))
            .nickname(input.getNickname())
            .status(ACTIVE)
            .isAdmin(false)
            .build();
        memberRepository.save(member);
        return true;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email).orElseThrow(
            () -> new UsernameNotFoundException("존재하지 않는 이메일입니다."));

        if (member.getStatus().equals(STOP)) {
            throw new MemberStatusStopException();
        }

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        if (member.getIsAdmin()) {
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        return User.builder()
            .username(member.getEmail())
            .password(member.getPassword())
            .authorities(grantedAuthorities)
            .build();
    }
}
