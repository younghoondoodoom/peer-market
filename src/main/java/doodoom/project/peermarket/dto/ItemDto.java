package doodoom.project.peermarket.dto;

import doodoom.project.peermarket.domain.Item;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemDto {

    private String name;
    private String description;
    private Long price;
    private String imgName;
    private Long memberId;
    private String memberNickname;

    public static ItemDto of(Item item) {
        return ItemDto.builder()
            .name(item.getName())
            .description(item.getDescription())
            .price(item.getPrice())
            .imgName(item.getImgName())
            .memberId(item.getMember().getId())
            .memberNickname(item.getMember().getNickname())
            .build();
    }
}
