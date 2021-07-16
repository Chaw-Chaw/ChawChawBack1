package okky.team.chawchaw.user;

import lombok.RequiredArgsConstructor;
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

    @Override
    @Transactional(readOnly = false)
    public UserDto createUser(UserDto userDto) {
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        UserEntity user = DtoToEntity.userDto(userDto);
        userRepository.save(user);
        return EntityToDto.userEntity(user);
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
    @Transactional(readOnly = false)
    public UserDto updateCountry(UserDto userDto) {
        Optional<UserEntity> user = userRepository.findById(userDto.getId());
        UserEntity userEntity = null;
        if (user.isPresent()) {
            userEntity = user.get();
            String country = userEntity.getCountry();
            if (country.isEmpty()) {
                userEntity.changeCountry(country + userDto.getCountry());
            }else{
                userEntity.changeCountry(country + "," + userDto.getCountry());
            }
            return EntityToDto.userEntity(userEntity);
        }
        return null;
    }

    @Override
    @Transactional(readOnly = false)
    public UserDto updateLanguage(UserDto userDto) {
        Optional<UserEntity> user = userRepository.findById(userDto.getId());
        UserEntity userEntity = null;
        if (user.isPresent()) {
            userEntity = user.get();
            String country = userEntity.getLanguage();
            if (country.isEmpty()) {
                userEntity.changeLanguage(country + userDto.getLanguage());
            }else{
                userEntity.changeLanguage(country + "," + userDto.getLanguage());
            }
            return EntityToDto.userEntity(userEntity);
        }
        return null;
    }

    @Override
    @Transactional(readOnly = false)
    public UserDto updateHopeLanguage(UserDto userDto) {
        Optional<UserEntity> user = userRepository.findById(userDto.getId());
        UserEntity userEntity = null;
        if (user.isPresent()) {
            userEntity = user.get();
            String country = userEntity.getHopeLanguage();
            if (country.isEmpty()) {
                userEntity.changeHopeLanguage(country + userDto.getHopeLanguage());
            }else{
                userEntity.changeHopeLanguage(country + "," + userDto.getHopeLanguage());
            }
            return EntityToDto.userEntity(userEntity);
        }
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
            return EntityToDto.userEntity(userEntity);
        }
        return null;
    }

    @Override
    @Transactional(readOnly = false)
    public UserDto updateSocialUrl(UserDto userDto) {
        Optional<UserEntity> user = userRepository.findById(userDto.getId());
        UserEntity userEntity = null;
        if (user.isPresent()) {
            userEntity = user.get();
            String country = userEntity.getSocialUrl();
            if (country.isEmpty()) {
                userEntity.changeSocialUrl(country + userDto.getSocialUrl());
            }else{
                userEntity.changeSocialUrl(country + "," + userDto.getSocialUrl());
            }
            return EntityToDto.userEntity(userEntity);
        }
        return null;
    }

    @Override
    @Transactional(readOnly = false)
    public UserDto updateImageUrl(UserDto userDto) {
        Optional<UserEntity> user = userRepository.findById(userDto.getId());
        UserEntity userEntity = null;
        if (user.isPresent()) {
            userEntity = user.get();
            String country = userEntity.getImageUrl();
            if (country.isEmpty()) {
                userEntity.changeImageUrl(country + userDto.getImageUrl());
            }else{
                userEntity.changeImageUrl(country + "," + userDto.getImageUrl());
            }
            return EntityToDto.userEntity(userEntity);
        }
        return null;
    }

    @Override
    @Transactional(readOnly = false)
    public UserDto deleteCountry(UserDto userDto) {
        Optional<UserEntity> user = userRepository.findById(userDto.getId());
        UserEntity userEntity = null;
        if (user.isPresent()) {
            userEntity = user.get();
            String country = Arrays.stream(userEntity.getCountry().split(","))
                    .filter(s -> !s.equals(userDto.getCountry()))
                    .collect(Collectors.joining(","));
            userEntity.changeCountry(country);
            return EntityToDto.userEntity(userEntity);
        }
        return null;
    }

    @Override
    @Transactional(readOnly = false)
    public UserDto deleteLanguage(UserDto userDto) {
        Optional<UserEntity> user = userRepository.findById(userDto.getId());
        UserEntity userEntity = null;
        if (user.isPresent()) {
            userEntity = user.get();
            String language = Arrays.stream(userEntity.getLanguage().split(","))
                    .filter(s -> !s.equals(userDto.getLanguage()))
                    .collect(Collectors.joining(","));
            userEntity.changeLanguage(language);
            return EntityToDto.userEntity(userEntity);
        }
        return null;
    }

    @Override
    @Transactional(readOnly = false)
    public UserDto deleteHopeLanguage(UserDto userDto) {
        Optional<UserEntity> user = userRepository.findById(userDto.getId());
        UserEntity userEntity = null;
        if (user.isPresent()) {
            userEntity = user.get();
            String hopeLanguage = Arrays.stream(userEntity.getHopeLanguage().split(","))
                    .filter(s -> !s.equals(userDto.getHopeLanguage()))
                    .collect(Collectors.joining(","));
            userEntity.changeHopeLanguage(hopeLanguage);
            return EntityToDto.userEntity(userEntity);
        }
        return null;
    }

    @Override
    @Transactional(readOnly = false)
    public UserDto deleteSocialUrl(UserDto userDto) {
        Optional<UserEntity> user = userRepository.findById(userDto.getId());
        UserEntity userEntity = null;
        if (user.isPresent()) {
            userEntity = user.get();
            String socialUrl = Arrays.stream(userEntity.getSocialUrl().split(","))
                    .filter(s -> !s.equals(userDto.getSocialUrl()))
                    .collect(Collectors.joining(","));
            userEntity.changeSocialUrl(socialUrl);
            return EntityToDto.userEntity(userEntity);
        }
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
            return EntityToDto.userEntity(userEntity);
        }
        return null;
    }
}
