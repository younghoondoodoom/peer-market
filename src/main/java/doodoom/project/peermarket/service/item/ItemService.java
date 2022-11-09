package doodoom.project.peermarket.service.item;

import doodoom.project.peermarket.dto.ItemDto;
import doodoom.project.peermarket.dto.ItemRegisterInput;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemService {

    boolean register(ItemRegisterInput input, String email);

    Page<ItemDto> list(Pageable pageable);
}
