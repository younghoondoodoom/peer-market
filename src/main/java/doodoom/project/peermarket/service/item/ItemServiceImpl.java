package doodoom.project.peermarket.service.item;

import static doodoom.project.peermarket.type.ItemStatus.ON_SALE;

import doodoom.project.peermarket.core.file.FileLoader;
import doodoom.project.peermarket.domain.Item;
import doodoom.project.peermarket.domain.Member;
import doodoom.project.peermarket.dto.ItemDto;
import doodoom.project.peermarket.dto.ItemRegisterInput;
import doodoom.project.peermarket.exception.member.MemberNotFoundException;
import doodoom.project.peermarket.repository.item.ItemRepository;
import doodoom.project.peermarket.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    public Page<ItemDto> list(Pageable pageable) {
        Page<Item> items = itemRepository.findItemsOnSaleAndMemberActive(pageable);
        return items.map(ItemDto::of);
    }

}
