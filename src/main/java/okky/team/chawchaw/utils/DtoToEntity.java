package okky.team.chawchaw.utils;

import okky.team.chawchaw.user.UserEntity;
import okky.team.chawchaw.user.dto.RequestUserVo;
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

    public static UserEntity RequestUserVoToEntity(RequestUserVo requestUserVo) {
        return UserEntity.builder()
                .email(requestUserVo.getEmail())
                .password(requestUserVo.getPassword())
                .name(requestUserVo.getName())
                .web_email(requestUserVo.getWeb_email())
                .school(requestUserVo.getSchool())
                .imageUrl(requestUserVo.getImageUrl())
                .content(requestUserVo.getContent())
                .facebookUrl(requestUserVo.getFacebookUrl())
                .instagramUrl(requestUserVo.getInstagramUrl())
                .build();
    }

    public static UserEntity CreateUserVoToEntity(RequestUserVo requestUserVo) {
        return UserEntity.builder()
                .email(requestUserVo.getEmail())
                .password(requestUserVo.getPassword())
                .name(requestUserVo.getName())
                .web_email(requestUserVo.getWeb_email())
                .school(requestUserVo.getSchool())
                .imageUrl(requestUserVo.getImageUrl())
                .content(requestUserVo.getContent())
                .facebookUrl(requestUserVo.getFacebookUrl())
                .instagramUrl(requestUserVo.getInstagramUrl())
                .build();
    }

}
