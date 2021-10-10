package okky.team.chawchaw.statistics.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter @Setter
@AllArgsConstructor
public class HopeLanguageUsersDto implements Serializable {

    String hopeLanguageName;
    Long userCnt;

}
