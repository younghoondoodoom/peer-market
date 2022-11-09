package doodoom.project.peermarket.service.item;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import doodoom.project.peermarket.core.file.FileLoader;
import doodoom.project.peermarket.domain.Item;
import doodoom.project.peermarket.domain.Member;
import doodoom.project.peermarket.dto.ItemDto;
import doodoom.project.peermarket.dto.ItemRegisterInput;
import doodoom.project.peermarket.exception.member.MemberNotFoundException;
import doodoom.project.peermarket.repository.item.ItemRepository;
import doodoom.project.peermarket.repository.member.MemberRepository;
import doodoom.project.peermarket.type.ItemStatus;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private FileLoader fileLoader;
    @InjectMocks
    private ItemServiceImpl itemService;

    @Test
    public void registerFailure() {
        //given
        MultipartFile file = new MockMultipartFile("file", new byte[]{});
        ItemRegisterInput input = ItemRegisterInput.builder()
            .name("name")
            .description("description")
            .price(10000L)
            .file(file)
            .build();
        given(memberRepository.findByEmail("email")).willReturn(Optional.empty());

        //then
        assertThrows(MemberNotFoundException.class,
            () -> itemService.register(input, "email")
        );
    }

    @Test
    public void registerSuccess() {
        //given
        MultipartFile file = new MockMultipartFile("file", new byte[]{});
        ItemRegisterInput input = ItemRegisterInput.builder()
            .name("name")
            .description("description")
            .price(10000L)
            .file(file)
            .build();
        Member member = Member.builder().build();
        given(memberRepository.findByEmail(anyString())).willReturn(Optional.of(member));
        given(fileLoader.upload(any(MultipartFile.class))).willReturn(anyString());
        //when
        boolean result = itemService.register(input, "email");

        //then
        assertThat(result).isTrue();
    }

    @Test
    public void listSuccess() {
        //given
        Member member = Member.builder()
            .id(1L)
            .items(new ArrayList<>())
            .nickname("test")
            .build();
        List<Item> items = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            Item item = Item.builder()
                .id((long) i)
                .name("name" + i)
                .description("description" + i)
                .imgName("imgName" + i)
                .price(10000L)
                .status(ItemStatus.ON_SALE)
                .member(member)
                .build();
            items.add(item);
        }

        given(itemRepository.findItemsOnSaleAndMemberActive(any(Pageable.class)))
            .willReturn(new PageImpl<>(items, PageRequest.of(0, 3), items.size()));

        //when
        Page<ItemDto> result = itemService.list(PageRequest.of(0, 3));

        //then
        assertThat(result.getTotalElements()).isEqualTo(3L);
        assertThat(result.getNumber()).isEqualTo(0);
        assertThat(result.getSize()).isEqualTo(3);
        assertThat(result.getContent().get(0).getName()).isEqualTo(items.get(0).getName());
    }
}