package doodoom.project.peermarket.service.member;

import doodoom.project.peermarket.domain.Member;
import doodoom.project.peermarket.dto.MemberRegisterInput;
import doodoom.project.peermarket.exception.MemberException;
import doodoom.project.peermarket.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static doodoom.project.peermarket.type.ErrorCode.EMAIL_ALREADY_EXIST;
import static doodoom.project.peermarket.type.MemberStatus.ACTIVE;

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
            throw new MemberException(EMAIL_ALREADY_EXIST);
        }
        Member member = Member.builder()
                .email(input.getEmail())
                .password(passwordEncoder.encode(input.getPassword()))
                .nickname(input.getNickname())
                .status(ACTIVE)
                .build();
        memberRepository.save(member);
        return true;
    }
}
