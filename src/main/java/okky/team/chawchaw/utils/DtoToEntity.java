package okky.team.chawchaw.utils;

import okky.team.chawchaw.user.UserEntity;
import okky.team.chawchaw.user.dto.UserDto;

public class DtoToEntity {

    public static UserEntity userDtoToEntity(UserDto userDto){
        return UserEntity
                .builder()
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .name(userDto.getName())
                .web_email(userDto.getWeb_email())
                .school(userDto.getSchool())
                .imageUrl(userDto.getImageUrl())
                .content(userDto.getContent())
                .facebookUrl(userDto.getFacebookUrl())
                .instagramUrl(userDto.getInstagramUrl())
                .build();
    }

}
