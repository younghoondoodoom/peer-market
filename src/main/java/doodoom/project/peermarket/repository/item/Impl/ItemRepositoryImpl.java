package doodoom.project.peermarket.repository.item.Impl;

import static doodoom.project.peermarket.domain.QItem.item;
import static doodoom.project.peermarket.domain.QMember.member;
import static doodoom.project.peermarket.type.ItemStatus.ON_SALE;
import static doodoom.project.peermarket.type.MemberStatus.ACTIVE;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import doodoom.project.peermarket.domain.Item;
import doodoom.project.peermarket.repository.item.CustomItemRepository;
import doodoom.project.peermarket.type.ItemStatus;
import doodoom.project.peermarket.type.MemberStatus;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

@RequiredArgsConstructor
public class ItemRepositoryImpl implements CustomItemRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Item> findItemsOnSaleAndMemberActive(final Pageable pageable) {
        List<Item> content = queryFactory
            .selectFrom(item)
            .where(isServiceableItem())
            .join(item.member, member).fetchJoin()
            .fetch();

        JPAQuery<Item> countQuery = queryFactory
            .selectFrom(item)
            .where(isServiceableItem())
            .join(item.member, member).fetchJoin();

        return PageableExecutionUtils.getPage(content, pageable,
            () -> (long) countQuery.fetch().size());
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
