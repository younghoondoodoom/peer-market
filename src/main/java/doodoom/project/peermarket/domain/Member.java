package doodoom.project.peermarket.domain;

import doodoom.project.peermarket.domain.base.BaseTimeEntity;
import doodoom.project.peermarket.type.MemberStatus;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", nullable = false)
    private Long id;
    private String email;
    private String password;
    private String nickname;

    @Enumerated(EnumType.STRING)
    private MemberStatus status;
    private Boolean isAdmin;

    @OneToMany(mappedBy = "member")
    List<Item> items = new ArrayList<>();
}
