package okky.team.chawchaw.statistics.log.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class CreateSearchLogDto {

    Long userId;
    String languageAbbr;

}
