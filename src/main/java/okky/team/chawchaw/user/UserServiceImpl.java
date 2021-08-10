package okky.team.chawchaw.user;

import lombok.RequiredArgsConstructor;
import okky.team.chawchaw.follow.FollowRepository;
import okky.team.chawchaw.user.country.CountryRepository;
import okky.team.chawchaw.user.country.UserCountryEntity;
import okky.team.chawchaw.user.country.UserCountryRepository;
import okky.team.chawchaw.user.dto.*;
import okky.team.chawchaw.user.language.*;
import okky.team.chawchaw.utils.DtoToEntity;
import okky.team.chawchaw.utils.exception.DuplicationUserEmailException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
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
    private final Environment env;
    private final RedisTemplate redisTemplate;
    @Value("${user.profile.image.path}")
    private String uploadPath;
    @Value("${user.profile.image.default}")
    private String defaultImage;

    @Override
    @Transactional(readOnly = false)
    public Long createUser(CreateUserDto createUserDto) {

        if (duplicateEmail(createUserDto.getEmail())) {
            throw new DuplicationUserEmailException();
        }

        createUserDto.setPassword(passwordEncoder.encode(createUserDto.getPassword()));
        if (!StringUtils.hasText(createUserDto.getImageUrl())) {
                createUserDto.setImageUrl(defaultImage);
        }
        UserEntity user = DtoToEntity.createUserDtoToEntity(createUserDto);

        return userRepository.save(user).getId();
    }

    @Override
    public Boolean duplicateEmail(String email) {
        Optional<UserEntity> user = userRepository.findByEmail(email);
        return user.isPresent();
    }

    @Override
    @Transactional(readOnly = false)
    @CacheEvict(value = "userDetail", key = "#userId")
    public void deleteUser(Long userId) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("not found user"));
        followRepository.deleteByUserFromOrUserTo(user, user);
        if (user != null)
            userRepository.delete(user);

    }

    @Override
    public List<UserCardDto> findUserCards(FindUserVo findUserVo) {
        return userRepository.findAllByElement(findUserVo);
    }

    @Override
    @Transactional(readOnly = false)
    @Cacheable(value = "userDetail", key = "#userId")
    public UserDetailsDto findUserDetails(Long userId) {

        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("not found user"));

        List<UserCountryEntity> countrys = userCountryRepository.findByUser(user);
        List<UserLanguageEntity> languages = userLanguageRepository.findByUser(user);
        List<UserHopeLanguageEntity> hopeLanguages = userHopeLanguageRepository.findByUser(user);
        Long follows = followRepository.countByUserToId(user.getId());

        UserDetailsDto result = UserDetailsDto.builder()
                .id(user.getId())
                .name(user.getName())
                .imageUrl(user.getImageUrl())
                .content(user.getContent())
                .facebookUrl(user.getFacebookUrl())
                .instagramUrl(user.getInstagramUrl())
                .days(user.getRegDate())
                .views(user.getViews())
                .follows(follows)
                .country(countrys.stream().map(x -> x.getCountry().getName()).collect(Collectors.toList()))
                .language(languages.stream().map(x -> x.getLanguage().getAbbr()).collect(Collectors.toList()))
                .hopeLanguage(hopeLanguages.stream().map(x -> x.getHopeLanguage().getAbbr()).collect(Collectors.toList()))
                .build();

        return result;
    }

    @Override
    public UserProfileDto findUserProfile(String email) {
        UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("not found user"));

        List<UserCountryEntity> countrys = userCountryRepository.findByUser(user);
        List<UserLanguageEntity> languages = userLanguageRepository.findByUser(user);
        List<UserHopeLanguageEntity> hopeLanguages = userHopeLanguageRepository.findByUser(user);

        UserProfileDto result = UserProfileDto.builder()
                .name(user.getName())
                .school(user.getSchool())
                .imageUrl(user.getImageUrl())
                .content(user.getContent())
                .repCountry(user.getRepCountry())
                .repLanguage(user.getRepLanguage())
                .repHopeLanguage(user.getRepHopeLanguage())
                .country(countrys.stream().map(x -> x.getCountry().getName()).collect(Collectors.toList()))
                .language(languages.stream().map(x -> x.getLanguage().getAbbr()).collect(Collectors.toList()))
                .hopeLanguage(hopeLanguages.stream().map(x -> x.getHopeLanguage().getAbbr()).collect(Collectors.toList()))
                .facebookUrl(user.getFacebookUrl())
                .instagramUrl(user.getInstagramUrl())
                .build();

        return result;
    }

    @Override
    @Transactional(readOnly = false)
    @CachePut(value = "userDetail", key = "#updateUserDto.id")
    public UserDetailsDto updateProfile(UpdateUserDto updateUserDto) {

        UserEntity user = userRepository.findById(updateUserDto.getId()).orElseThrow(() -> new UsernameNotFoundException("not found user"));
        Long follows = followRepository.countByUserToId(user.getId());

        if (user.getRole().equals(Role.GUEST)) {
            user.changeRole(Role.USER);
        }
        user.changeContent(updateUserDto.getContent());
        user.changeFacebookUrl(updateUserDto.getFacebookUrl());
        user.changeInstagramUrl(updateUserDto.getInstagramUrl());
        user.changeRepCountry(updateUserDto.getRepCountry());
        user.changeRepLanguage(updateUserDto.getRepLanguage());
        user.changeRepHopeLanguage(updateUserDto.getRepHopeLanguage());

        List<UserCountryEntity> userCountrys = userCountryRepository.findByUser(user);
        List<UserLanguageEntity> userLanguages = userLanguageRepository.findByUser(user);
        List<UserHopeLanguageEntity> userHopeLanguages = userHopeLanguageRepository.findByUser(user);

        Set<String> addCountrys = updateUserDto.getCountry();
        Set<String> removeCountrys = userCountrys.stream().map(x -> x.getCountry().getName()).collect(Collectors.toSet());

        Set<String> addLanguages = updateUserDto.getLanguage();
        Set<String> removeLanguages = userLanguages.stream().map(x -> x.getLanguage().getAbbr()).collect(Collectors.toSet());

        Set<String> addHopeLanguages = updateUserDto.getHopeLanguage();
        Set<String> removeHopeLanguages = userHopeLanguages.stream().map(x -> x.getHopeLanguage().getAbbr()).collect(Collectors.toSet());

        removeCountrys.removeAll(addCountrys);
        addCountrys.removeAll(userCountrys.stream().map(x -> x.getCountry().getName()).collect(Collectors.toSet()));

        removeLanguages.removeAll(addLanguages);
        addLanguages.removeAll(userLanguages.stream().map(x -> x.getLanguage().getAbbr()).collect(Collectors.toSet()));

        removeHopeLanguages.removeAll(addHopeLanguages);
        addHopeLanguages.removeAll(userHopeLanguages.stream().map(x -> x.getHopeLanguage().getAbbr()).collect(Collectors.toSet()));

        for (String removeCountry : removeCountrys) {
            userCountryRepository.deleteByUserAndCountry(user, countryRepository.findByName(removeCountry));
        }
        for (String addCountry : addCountrys) {
            userCountryRepository.save(new UserCountryEntity(user, countryRepository.findByName(addCountry)));
        }

        for (String removeLanguage : removeLanguages) {
            userLanguageRepository.deleteByUserAndLanguage(user, languageRepository.findByAbbr(removeLanguage));
        }
        for (String addLanguage : addLanguages) {
            userLanguageRepository.save(new UserLanguageEntity(user, languageRepository.findByAbbr(addLanguage)));
        }

        for (String removeHopeLanguage : removeHopeLanguages) {
            userHopeLanguageRepository.deleteByUserAndHopeLanguage(user, languageRepository.findByAbbr(removeHopeLanguage));
        }
        for (String addHopeLanguage : addHopeLanguages) {
            userHopeLanguageRepository.save(new UserHopeLanguageEntity(user, languageRepository.findByAbbr(addHopeLanguage)));
        }

        UserDetailsDto result = UserDetailsDto.builder()
                .id(user.getId())
                .name(user.getName())
                .imageUrl(user.getImageUrl())
                .content(user.getContent())
                .facebookUrl(user.getFacebookUrl())
                .instagramUrl(user.getInstagramUrl())
                .days(user.getRegDate())
                .views(user.getViews())
                .follows(follows)
                .country(userCountryRepository.findByUser(user).stream().map(x -> x.getCountry().getName()).collect(Collectors.toList()))
                .language(userLanguageRepository.findByUser(user).stream().map(x -> x.getLanguage().getAbbr()).collect(Collectors.toList()))
                .hopeLanguage(userHopeLanguageRepository.findByUser(user).stream().map(x -> x.getHopeLanguage().getAbbr()).collect(Collectors.toList()))
                .build();

        return result;
    }

    @Override
    @Transactional(readOnly = false)
    public String uploadImage(String imageUrl, Long userId) {
        try {

            URL url = new URL(imageUrl);
            BufferedImage img = ImageIO.read(url);
            UserEntity user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("not found user"));
            String uuid = UUID.randomUUID().toString();
            String fileName = ".jpg";

            /* 폴더 생성 */
            String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            String folderPath = date.replace("//", File.separator);
            File uploadPathFolder = new File(uploadPath, folderPath);
            if (!uploadPathFolder.exists()) {
                uploadPathFolder.mkdirs();
            }

            String saveName = uploadPath + File.separator + folderPath + File.separator + uuid + "_" + fileName;
            Path savePath = Paths.get(saveName);
            File file = new File(String.valueOf(savePath));
            ImageIO.write(img, "jpg", file);
            String encodeUrl = URLEncoder.encode(folderPath + File.separator + uuid + "_" + fileName, "UTF-8");
            if (!URLDecoder.decode(user.getImageUrl(), "UTF-8").equals(defaultImage)) {
                new File(uploadPath + URLDecoder.decode(user.getImageUrl(), "UTF-8")).delete();
            }
            user.changeImageUrl(encodeUrl);
            return encodeUrl;
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    @Transactional(readOnly = false)
    public String uploadImage(MultipartFile file, Long userId) {
        try {
            UserEntity user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("not found user"));
            String uuid = UUID.randomUUID().toString();
            String fileName = file.getOriginalFilename();

            /* 폴더 생성 */
            String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            String folderPath = date.replace("//", File.separator);
            File uploadPathFolder = new File(uploadPath, folderPath);
            if (!uploadPathFolder.exists()) {
                uploadPathFolder.mkdirs();
            }

            String saveName = uploadPath + File.separator + folderPath + File.separator + uuid + "_" + fileName;
            Path savePath = Paths.get(saveName);
            file.transferTo(savePath);
            String encodeUrl = URLEncoder.encode(folderPath + File.separator + uuid + "_" + fileName, "UTF-8");
            if (!URLDecoder.decode(user.getImageUrl(), "UTF-8").equals(defaultImage)) {
                new File(uploadPath + URLDecoder.decode(user.getImageUrl(), "UTF-8")).delete();
            }
            user.changeImageUrl(encodeUrl);
            return encodeUrl;
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    @Transactional(readOnly = false)
    public String deleteImage(String imageUrl, Long userId) {
        try {
            UserEntity user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("not found user"));
            if (!imageUrl.equals(defaultImage)) {
                user.changeImageUrl(defaultImage);
                new File(uploadPath + URLDecoder.decode(imageUrl, "UTF-8")).delete();
                return defaultImage;
            } else{
                return "";
            }
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    public Boolean isUser(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    @Override
    @Transactional(readOnly = false)
    public void checkView(Long userFrom, Long userTo) {
        System.out.println(redisTemplate.opsForValue().get("view::" + userFrom + "_" + userTo));
        if (redisTemplate.opsForValue().get("view::" + userFrom + "_" + userTo) == null) {
            UserEntity user = userRepository.findById(userTo).orElseThrow(() -> new UsernameNotFoundException("not found user"));
            user.plusViews();
            redisTemplate.opsForValue().set("view::" + userFrom + "_" + userTo, 1);
            redisTemplate.expireAt("view::" + userFrom + "_" + userTo, Date.from(ZonedDateTime.now().plusDays(1).toInstant()));
        }
    }
}
