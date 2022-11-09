package doodoom.project.peermarket.repository.item;

import doodoom.project.peermarket.domain.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomItemRepository {

    Page<Item> findItemsOnSaleAndMemberActive(final Pageable pageable);
}
