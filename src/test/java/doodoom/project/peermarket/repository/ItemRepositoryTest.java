package doodoom.project.peermarket.repository;

import static doodoom.project.peermarket.type.ItemStatus.ON_SALE;
import static doodoom.project.peermarket.type.MemberStatus.ACTIVE;
import static org.assertj.core.api.Assertions.assertThat;

import doodoom.project.peermarket.config.QuerydslTestConfig;
import doodoom.project.peermarket.domain.Item;
import doodoom.project.peermarket.domain.Member;
import doodoom.project.peermarket.repository.item.ItemRepository;
import doodoom.project.peermarket.repository.member.MemberRepository;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@DataJpaTest
@Import(QuerydslTestConfig.class)
public class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private MemberRepository memberRepository;

    private static final List<Item> items = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        Member member = Member.builder()
            .nickname("member")
            .status(ACTIVE)
            .items(new ArrayList<>())
            .build();
        for (int i = 0; i < 3; i++) {
            Item item = Item.builder()
                .name("name" + i)
                .description("description" + i)
                .imgName("imgName" + i)
                .price(10000L)
                .status(ON_SALE)
                .build();
            item.changeMember(member);
            items.add(item);
        }
        memberRepository.save(member);
        itemRepository.saveAll(items);
    }

    @Test
    public void findItemsOnSaleAndMemberActive() {
        //given
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by("name").descending());

        //when
        Page<Item> result = itemRepository.findItemsOnSaleAndMemberActive(pageRequest);

        //then
        assertThat(result.getTotalElements()).isEqualTo(3L);
        assertThat(result.getNumber()).isEqualTo(0);
        assertThat(result.getTotalPages()).isEqualTo(1);
        assertThat(result.getSize()).isEqualTo(3);
        for (int i = 0; i < 3; i++) {
            assertThat(result.getContent().get(i)).isEqualTo(items.get(items.size() - (i + 1)));
        }
    }
}
