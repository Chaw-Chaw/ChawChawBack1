package okky.team.chawchaw.admin.dto;

import lombok.*;
import okky.team.chawchaw.block.dto.BlockUserDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailDto {

    Long id;
    String name;
    String imageUrl;
    String content;
    String repCountry;
    String repLanguage;
    String repHopeLanguage;
    List<String> country;
    List<String> language;
    List<String> hopeLanguage;
    String facebookUrl;
    String instagramUrl;
    LocalDateTime days;
    Long views;
    Long likes;
    Boolean isLike;
    List<BlockUserDto> blockUsers = new ArrayList<>();

}
