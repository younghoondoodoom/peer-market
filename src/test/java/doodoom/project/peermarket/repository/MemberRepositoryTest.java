package doodoom.project.peermarket.repository;

import static org.assertj.core.api.Assertions.assertThat;

import doodoom.project.peermarket.configuration.JpaAuditingConfig;
import doodoom.project.peermarket.domain.Member;
import doodoom.project.peermarket.repository.member.MemberRepository;
import doodoom.project.peermarket.type.MemberStatus;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@Import(JpaAuditingConfig.class)
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

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
    public void save() {
        //given
        Member member = members.get(0);
        //when
        memberRepository.save(member);

        //then
        List<Member> findAll = memberRepository.findAll();
        assertThat(findAll.isEmpty()).isFalse();
        assertThat(findAll.get(0).getEmail()).isEqualTo(member.getEmail());
        assertThat(findAll.get(0).getPassword()).isEqualTo(member.getPassword());
        assertThat(findAll.get(0).getStatus()).isEqualTo(member.getStatus());
    }

    @Test
    public void findByEmail() {
        //given
        Member member = members.get(0);
        memberRepository.save(member);

        //when
        Optional<Member> optionalMember =
            memberRepository.findByEmail(member.getEmail());

        //then
        assertThat(optionalMember.isPresent()).isTrue();
        assertThat(optionalMember.get().getEmail()).isEqualTo(member.getEmail());
        assertThat(optionalMember.get().getPassword()).isEqualTo(member.getPassword());
        assertThat(optionalMember.get().getStatus()).isEqualTo(member.getStatus());
    }

    @Test
    @Rollback(value = false)
    public void entityListeners() {
        //given
        Member member = members.get(0);

        //when
        memberRepository.save(member);

        //then
        Optional<Member> optionalMember =
            memberRepository.findByEmail(member.getEmail());
        assertThat(optionalMember.isPresent()).isTrue();
        assertThat(optionalMember.get().getCreatedAt()).isNotNull();
        assertThat(optionalMember.get().getLastModifiedAt()).isNotNull();
    }
}