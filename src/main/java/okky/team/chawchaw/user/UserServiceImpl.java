package okky.team.chawchaw.user;

import lombok.RequiredArgsConstructor;
import okky.team.chawchaw.follow.FollowRepository;
import okky.team.chawchaw.user.country.CountryEntity;
import okky.team.chawchaw.user.country.CountryRepository;
import okky.team.chawchaw.user.country.UserCountryEntity;
import okky.team.chawchaw.user.country.UserCountryRepository;
import okky.team.chawchaw.user.dto.CreateUserVo;
import okky.team.chawchaw.user.dto.RequestUserVo;
import okky.team.chawchaw.user.dto.UserDetailsDto;
import okky.team.chawchaw.user.dto.UserDto;
import okky.team.chawchaw.user.language.*;
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
    private final CountryRepository countryRepository;
    private final UserCountryRepository userCountryRepository;
    private final LanguageRepository languageRepository;
    private final UserLanguageRepository userLanguageRepository;
    private final UserHopeLanguageRepository userHopeLanguageRepository;

    @Override
    @Transactional(readOnly = false)
    public Boolean createUser(CreateUserVo createUserVo) {
        createUserVo.setPassword(passwordEncoder.encode(createUserVo.getPassword()));
        UserEntity user = DtoToEntity.CreateUserVoToEntity(createUserVo);
        userRepository.save(user);

        user.changeFacebookUrl(createUserVo.getFacebookUrl());
        user.changeInstagramUrl(createUserVo.getInstagramUrl());

        for (String country : createUserVo.getCountry()) {
            UserCountryEntity userCountryEntity = userCountryRepository.save(
                    new UserCountryEntity(
                            user, countryRepository.findByName(country)
                    )
            );
            userCountryEntity.changeUser(user);
        }
        for (String language : createUserVo.getLanguage()) {
            UserLanguageEntity userLanguageEntity = userLanguageRepository.save(
                    new UserLanguageEntity(
                            user, languageRepository.findByAbbr(language)
                    )
            );
            userLanguageEntity.changeUser(user);
        }
        for (String language : createUserVo.getHopeLanguage()) {
            UserHopeLanguageEntity userHopeLanguageEntity = userHopeLanguageRepository.save(
                    new UserHopeLanguageEntity(
                            user, languageRepository.findByAbbr(language)
                    )
            );
            userHopeLanguageEntity.changeUser(user);
        }
        return true;
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
        if (user != null)
            userRepository.delete(user);

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
    public Boolean updateCountry(RequestUserVo requestUserVo) {
        UserEntity user = userRepository.findById(requestUserVo.getId()).orElseThrow();
        CountryEntity country = countryRepository.findByName(requestUserVo.getCountry());
        if (user != null && country != null) {
            userCountryRepository.save(new UserCountryEntity(user, country));
            return true;
        }else
            return false;
    }

    @Override
    @Transactional(readOnly = false)
    public Boolean updateLanguage(RequestUserVo requestUserVo) {
        UserEntity user = userRepository.findById(requestUserVo.getId()).orElseThrow();
        LanguageEntity language = languageRepository.findByAbbr(requestUserVo.getLanguage());
        if (user != null && language != null) {
            userLanguageRepository.save(new UserLanguageEntity(user, language));
            return true;
        }else
            return false;
    }

    @Override
    @Transactional(readOnly = false)
    public Boolean updateHopeLanguage(RequestUserVo requestUserVo) {
        UserEntity user = userRepository.findById(requestUserVo.getId()).orElseThrow();
        LanguageEntity language = languageRepository.findByAbbr(requestUserVo.getHopeLanguage());
        if (user != null && language != null) {
            userHopeLanguageRepository.save(new UserHopeLanguageEntity(user, language));
            return true;
        }else
            return false;
    }

    @Override
    @Transactional(readOnly = false)
    public Boolean updateContent(RequestUserVo requestUserVo) {
        UserEntity user = userRepository.findById(requestUserVo.getId()).orElseThrow();
        if (user != null) {
            user.changeContent(requestUserVo.getContent());
            return true;
        }else
            return false;
    }

    @Override
    public Boolean updateFacebookUrl(RequestUserVo requestUserVo) {
        UserEntity user = userRepository.findById(requestUserVo.getId()).orElseThrow();

        String facebookUrl = requestUserVo.getFacebookUrl();

        if (facebookUrl != null) {
            user.changeFacebookUrl(requestUserVo.getFacebookUrl());
            return true;
        }else
            return false;
    }

    @Override
    public Boolean updateInstagramUrl(RequestUserVo requestUserVo) {
        UserEntity user = userRepository.findById(requestUserVo.getId()).orElseThrow();

        String instagramUrl = requestUserVo.getInstagramUrl();

        if (instagramUrl != null) {
            user.changeInstagramUrl(requestUserVo.getInstagramUrl());
            return true;
        }else
            return false;
    }

    @Override
    @Transactional(readOnly = false)
    public Boolean updateImageUrl(RequestUserVo requestUserVo) {
        UserEntity user = userRepository.findById(requestUserVo.getId()).orElseThrow();

        String imageUrl = requestUserVo.getImageUrl();

        if (imageUrl != null) {
            user.changeImageUrl(imageUrl);
            return true;
        }else
            return false;
    }

    @Override
    @Transactional(readOnly = false)
    public Boolean deleteCountry(RequestUserVo requestUserVo) {
        UserEntity user = userRepository.findById(requestUserVo.getId()).orElseThrow();
        CountryEntity country = countryRepository.findByName(requestUserVo.getCountry());
        if (country != null) {
            userCountryRepository.deleteByUserAndCountry(user, country);
            return true;
        }else
            return false;
    }

    @Override
    @Transactional(readOnly = false)
    public Boolean deleteLanguage(RequestUserVo requestUserVo) {
        UserEntity user = userRepository.findById(requestUserVo.getId()).orElseThrow();
        LanguageEntity language = languageRepository.findByAbbr(requestUserVo.getLanguage());

        if (language != null) {
            userLanguageRepository.deleteByUserAndLanguage(user, language);
            return true;
        }else
            return false;
    }

    @Override
    @Transactional(readOnly = false)
    public Boolean deleteHopeLanguage(RequestUserVo requestUserVo) {
        UserEntity user = userRepository.findById(requestUserVo.getId()).orElseThrow();
        LanguageEntity language = languageRepository.findByAbbr(requestUserVo.getHopeLanguage());

        if (language != null) {
            userHopeLanguageRepository.deleteByUserAndHopeLanguage(user, language);
            return true;
        }else
            return false;
    }

    @Override
    public Boolean deleteFacebookUrl(RequestUserVo requestUserVo) {
        UserEntity user = userRepository.findById(requestUserVo.getId()).orElseThrow();

        String facebookUrl = requestUserVo.getFacebookUrl();

        if (facebookUrl != null) {
            user.changeFacebookUrl("");
            return true;
        }
        else
            return false;
    }

    @Override
    public Boolean deleteInstagramUrl(RequestUserVo requestUserVo) {
        UserEntity user = userRepository.findById(requestUserVo.getId()).orElseThrow();

        String instagramUrl = requestUserVo.getInstagramUrl();

        if (instagramUrl != null) {
            user.changeInstagramUrl("");
            return true;
        }else
            return false;
    }

    @Override
    @Transactional(readOnly = false)
    public Boolean deleteImageUrl(RequestUserVo requestUserVo) {
        UserEntity user = userRepository.findById(requestUserVo.getId()).orElseThrow();

        if (user != null) {
            user.changeImageUrl("");
            return true;
        }else
            return false;
    }
}
