package doodoom.project.peermarket.service.item;

import doodoom.project.peermarket.core.file.FileLoader;
import doodoom.project.peermarket.domain.Item;
import doodoom.project.peermarket.domain.Member;
import doodoom.project.peermarket.dto.ItemRegisterInput;
import doodoom.project.peermarket.exception.member.MemberNotFoundException;
import doodoom.project.peermarket.repository.ItemRepository;
import doodoom.project.peermarket.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static doodoom.project.peermarket.type.ItemStatus.ON_SALE;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final FileLoader fileLoader;

    @Override
    public boolean register(ItemRegisterInput input, String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow(
                MemberNotFoundException::new);
        String imgName = fileLoader.upload(input.getFile());
        Item item = Item.builder()
                .member(member)
                .name(input.getName())
                .description(input.getDescription())
                .price(input.getPrice())
                .imgName(imgName)
                .status(ON_SALE)
                .build();
        itemRepository.save(item);
        return true;
    }
}
