package okky.team.chawchaw.utils;

import okky.team.chawchaw.user.UserEntity;
import okky.team.chawchaw.user.dto.UserDto;

public class DtoToEntity {

    public static UserEntity userDto(UserDto userDto){
        return UserEntity
                .builder()
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .name(userDto.getName())
                .web_email(userDto.getWeb_email())
                .school(userDto.getSchool())
                .content(userDto.getContent())
                .country(userDto.getCountry())
                .language(userDto.getLanguage())
                .hopeLanguage(userDto.getHopeLanguage())
                .build();
    }

}
