package okky.team.chawchaw.user;

import lombok.RequiredArgsConstructor;
import okky.team.chawchaw.follow.FollowRepository;
import okky.team.chawchaw.user.dto.UserDetailsDto;
import okky.team.chawchaw.user.dto.UserDto;
import okky.team.chawchaw.utils.DtoToEntity;
import okky.team.chawchaw.utils.EntityToDto;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService{

    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final FollowRepository followRepository;

    @Override
    @Transactional(readOnly = false)
    public UserDto createUser(UserDto userDto) {
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        UserEntity user = DtoToEntity.userDtoToEntity(userDto);
        userRepository.save(user);
        return EntityToDto.entityToUserDto(user);
    }

    @Override
    public Boolean duplicateEmail(String email) {
        List<UserEntity> users = userRepository.findByEmail(email);
        return !users.isEmpty();
    }

    @Override
    @Transactional(readOnly = false)
    public void deleteUser(String email) {
        List<UserEntity> users = userRepository.findByEmail(email);
        UserEntity user = users.get(0);
        if (user != null){
            userRepository.delete(user);
        }
    }

    @Override
    public UserDetailsDto findUserDetails(Long userId) {
        return null;
    }

    @Override
    public UserDetailsDto findUserProfile(Long userId) {
        return null;
    }

    @Override
    @Transactional(readOnly = false)
    public UserDto updateCountry(UserDto userDto) {
        return null;
    }

    @Override
    @Transactional(readOnly = false)
    public UserDto updateLanguage(UserDto userDto) {
        return null;
    }

    @Override
    @Transactional(readOnly = false)
    public UserDto updateHopeLanguage(UserDto userDto) {
        return null;
    }

    @Override
    @Transactional(readOnly = false)
    public UserDto updateContent(UserDto userDto) {
        Optional<UserEntity> user = userRepository.findById(userDto.getId());
        UserEntity userEntity = null;
        if (user.isPresent()) {
            userEntity = user.get();
            userEntity.changeContent(userDto.getContent());
            return EntityToDto.entityToUserDto(userEntity);
        }
        return null;
    }

    @Override
    @Transactional(readOnly = false)
    public UserDto updateSocialUrl(UserDto userDto) {
        return null;
    }

    @Override
    @Transactional(readOnly = false)
    public UserDto updateImageUrl(UserDto userDto) {
        return null;
    }

    @Override
    @Transactional(readOnly = false)
    public UserDto deleteCountry(UserDto userDto) {
        return null;
    }

    @Override
    @Transactional(readOnly = false)
    public UserDto deleteLanguage(UserDto userDto) {
        return null;
    }

    @Override
    @Transactional(readOnly = false)
    public UserDto deleteHopeLanguage(UserDto userDto) {
        return null;
    }

    @Override
    @Transactional(readOnly = false)
    public UserDto deleteSocialUrl(UserDto userDto) {
        return null;
    }

    @Override
    @Transactional(readOnly = false)
    public UserDto deleteImageUrl(UserDto userDto) {
        Optional<UserEntity> user = userRepository.findById(userDto.getId());
        UserEntity userEntity = null;
        if (user.isPresent()) {
            userEntity = user.get();
            userEntity.changeImageUrl("");
            return EntityToDto.entityToUserDto(userEntity);
        }
        return null;
    }
}
