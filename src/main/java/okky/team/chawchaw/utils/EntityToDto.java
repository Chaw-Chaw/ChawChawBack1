package okky.team.chawchaw.utils;

import okky.team.chawchaw.user.UserEntity;
import okky.team.chawchaw.user.dto.UserDetailsDto;
import okky.team.chawchaw.user.dto.UserDto;

public class EntityToDto {

    public static UserDto entityToUserDto(UserEntity userEntity){
        return UserDto.builder()
                .id(userEntity.getId())
                .email(userEntity.getEmail())
                .name(userEntity.getName())
                .web_email(userEntity.getWeb_email())
                .school(userEntity.getSchool())
                .imageUrl(userEntity.getImageUrl())
                .content(userEntity.getContent())
                .imageUrl(userEntity.getImageUrl())
                .facebookUrl(userEntity.getFacebookUrl())
                .instagramUrl(userEntity.getInstagramUrl())
                .views(userEntity.getViews())
                .regDate(userEntity.getRegDate())
                .build();
    }

    public static UserDetailsDto entityToUserDetailsDto(UserEntity userEntity){
        return UserDetailsDto.builder()
                .id(userEntity.getId())
                .name(userEntity.getName())
                .imageUrl(userEntity.getImageUrl())
                .facebookUrl(userEntity.getFacebookUrl())
                .instagramUrl(userEntity.getInstagramUrl())
                .content(userEntity.getContent())
                .days(userEntity.getRegDate())
                .views(userEntity.getViews())
                .build();
    }
}
