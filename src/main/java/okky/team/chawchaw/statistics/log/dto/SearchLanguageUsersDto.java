package okky.team.chawchaw.statistics.log.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter @Setter
@AllArgsConstructor
public class SearchLanguageUsersDto implements Serializable {

    String languageAbbr;
    Long userCnt;

}
