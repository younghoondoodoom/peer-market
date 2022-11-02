package doodoom.project.peermarket.service.item;

import doodoom.project.peermarket.core.file.FileLoader;
import doodoom.project.peermarket.domain.Member;
import doodoom.project.peermarket.dto.ItemRegisterInput;
import doodoom.project.peermarket.exception.member.MemberNotFoundException;
import doodoom.project.peermarket.repository.ItemRepository;
import doodoom.project.peermarket.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

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
}