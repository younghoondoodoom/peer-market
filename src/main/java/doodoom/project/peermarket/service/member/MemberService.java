package doodoom.project.peermarket.service.member;

import doodoom.project.peermarket.dto.MemberRegisterInput;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface MemberService extends UserDetailsService {
    boolean register(MemberRegisterInput input);
}
