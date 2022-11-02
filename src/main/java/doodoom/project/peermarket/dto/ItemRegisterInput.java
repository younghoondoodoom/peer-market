package doodoom.project.peermarket.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemRegisterInput {

    @NotEmpty(message = "제품명은 필수 입력 사항입니다.")
    @Size(max = 40, message = "제품명은 40자를 넘길 수 없습니다.")
    private String name;
    @NotEmpty(message = "제품 설명은 필수 입력 사항입니다.")
    @Size(max = 300, message = "제품 설명은 300자를 넘길 수 없습니다.")
    private String description;
    @NotEmpty(message = "가격은 필수 입력 사항입니다.")
    private Long price;
    @NotEmpty(message = "사진 파일은 필수 입력 사항입니다.")
    private MultipartFile file;
}
