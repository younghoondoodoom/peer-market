package doodoom.project.peermarket.repository.item.Impl;

import static doodoom.project.peermarket.domain.QItem.item;
import static doodoom.project.peermarket.domain.QMember.member;
import static doodoom.project.peermarket.type.ItemStatus.ON_SALE;
import static doodoom.project.peermarket.type.MemberStatus.ACTIVE;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import doodoom.project.peermarket.domain.Item;
import doodoom.project.peermarket.repository.item.CustomItemRepository;
import doodoom.project.peermarket.repository.support.Querydsl4RepositorySupport;
import doodoom.project.peermarket.type.ItemStatus;
import doodoom.project.peermarket.type.MemberStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class ItemRepositoryImpl extends Querydsl4RepositorySupport implements CustomItemRepository {

    public ItemRepositoryImpl() {
        super(Item.class);
    }

    @Override
    public Page<Item> findItemsOnSaleAndMemberActive(final Pageable pageable) {
        return applyPagination(pageable, query -> query
            .selectFrom(item)
            .where(isServiceableItem())
            .join(item.member, member).fetchJoin());
    }

    private Predicate isServiceableItem() {
        return itemStatusEq(ON_SALE).and(memberStatusEq(ACTIVE));
    }

    private BooleanExpression memberStatusEq(final MemberStatus memberStatus) {
        return item.member.status.eq(memberStatus);
    }

    private BooleanExpression itemStatusEq(final ItemStatus itemStatus) {
        return item.status.eq(itemStatus);
    }
}
