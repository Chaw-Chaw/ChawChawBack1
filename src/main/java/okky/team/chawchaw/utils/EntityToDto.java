package okky.team.chawchaw.utils;

import okky.team.chawchaw.user.UserEntity;
import okky.team.chawchaw.user.dto.UserDto;

public class EntityToDto {

    public static UserDto userEntity(UserEntity userEntity){
        return UserDto.builder()
                .id(userEntity.getId())
                .email(userEntity.getEmail())
                .name(userEntity.getName())
                .web_email(userEntity.getWeb_email())
                .school(userEntity.getSchool())
                .imageUrl(userEntity.getImageUrl())
                .content(userEntity.getContent())
                .country(userEntity.getCountry())
                .language(userEntity.getLanguage())
                .hopeLanguage(userEntity.getHopeLanguage())
                .imageUrl(userEntity.getImageUrl())
                .socialUrl(userEntity.getSocialUrl())
                .views(userEntity.getViews())
                .regDate(userEntity.getRegDate())
                .build();
    }
}
