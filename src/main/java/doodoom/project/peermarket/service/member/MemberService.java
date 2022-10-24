package doodoom.project.peermarket.service.member;

import doodoom.project.peermarket.dto.MemberRegisterInput;

public interface MemberService {
    boolean register(MemberRegisterInput input);
}
