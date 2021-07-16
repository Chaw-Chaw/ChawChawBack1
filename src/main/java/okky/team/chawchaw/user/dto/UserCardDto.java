package okky.team.chawchaw.user.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCardDto {

    Long id;
    String imageUrl;
    String content;
    LocalDateTime days;
    Long views;
    Long follows;

}
