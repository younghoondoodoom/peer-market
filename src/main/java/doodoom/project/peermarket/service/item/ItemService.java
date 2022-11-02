package doodoom.project.peermarket.service.item;

import doodoom.project.peermarket.dto.ItemRegisterInput;

public interface ItemService {

    boolean register(ItemRegisterInput input, String email);
}
