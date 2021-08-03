package okky.team.chawchaw.utils;

import okky.team.chawchaw.user.UserEntity;
import okky.team.chawchaw.user.dto.CreateUserDto;

public class DtoToEntity {

    public static UserEntity createUserDtoToEntity(CreateUserDto createUserDto) {
        return UserEntity.builder()
                .email(createUserDto.getEmail())
                .password(createUserDto.getPassword())
                .name(createUserDto.getName())
                .web_email(createUserDto.getWeb_email())
                .school(createUserDto.getSchool())
                .imageUrl(createUserDto.getImageUrl())
                .build();
    }

}
