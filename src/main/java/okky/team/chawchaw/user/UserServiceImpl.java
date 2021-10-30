package okky.team.chawchaw.user;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import okky.team.chawchaw.block.BlockRepository;
import okky.team.chawchaw.chat.room.ChatRoomRepository;
import okky.team.chawchaw.chat.room.ChatRoomUserRepository;
import okky.team.chawchaw.config.jwt.JwtTokenProvider;
import okky.team.chawchaw.config.jwt.TokenRedisRepository;
import okky.team.chawchaw.like.LikeRepository;
import okky.team.chawchaw.user.country.CountryRepository;
import okky.team.chawchaw.user.country.UserCountryEntity;
import okky.team.chawchaw.user.country.UserCountryRepository;
import okky.team.chawchaw.user.dto.*;
import okky.team.chawchaw.user.exception.ConnectElseWhereException;
import okky.team.chawchaw.user.language.*;
import okky.team.chawchaw.user.exception.DuplicationUserEmailException;
import okky.team.chawchaw.user.exception.PointMyselfException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService{

    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final BlockRepository blockRepository;
    private final CountryRepository countryRepository;
    private final UserCountryRepository userCountryRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomUserRepository chatRoomUserRepository;
    private final LanguageRepository languageRepository;
    private final UserLanguageRepository userLanguageRepository;
    private final UserHopeLanguageRepository userHopeLanguageRepository;
    private final AmazonS3 amazonS3;
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenRedisRepository tokenRedisRepository;
    private final UserRedisRepository userRedisRepository;
    @Value("${user.profile.image.default}")
    private String defaultImage;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    @Value("${cloud.front.domain}")
    private String cloudFrontDomain;

    @Override
    @Transactional(readOnly = false)
    public Long createUser(CreateUserDto createUserDto) {

        duplicateEmail(createUserDto.getEmail());

        createUserDto.setPassword(passwordEncoder.encode(createUserDto.getPassword()));
        if (!StringUtils.hasText(createUserDto.getImageUrl())) {
                createUserDto.setImageUrl(cloudFrontDomain + defaultImage);
        }
        UserEntity user = createUserDto.dtoToUserEntity();

        return userRepository.save(user).getId();
    }

    @Override
    public void duplicateEmail(String email) {
        Optional<UserEntity> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            throw new DuplicationUserEmailException();
        }
    }

    @Override
    @Transactional(readOnly = false)
    @CacheEvict(value = "userDetail", key = "#userId")
    public void deleteUser(Long userId) {

        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("not found user"));
        likeRepository.deleteByUserFromOrUserTo(user, user);
        blockRepository.deleteByUserFromOrUserTo(user, user);
        chatRoomRepository.deleteAll(chatRoomUserRepository.findChatRoomsByUserId(user.getId()));
        userRepository.delete(user);
    }

    @Override
    public List<UserCardDto> findUserCards(FindUserVo findUserVo) {
        List<UserCardDto> result = userRepository.findAllByElement(findUserVo);
        for (UserCardDto userCardDto : result) {
            userCardDto.setViews(getViews(userCardDto.getId()));
        }
        return result;
    }

    @Override
    @Transactional(readOnly = false)
    @Cacheable(value = "userDetail", key = "#userId")
    public UserDetailsDto findUserDetails(Long userId) {

        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("not found user"));

        List<UserCountryEntity> countrys = userCountryRepository.findByUser(user);
        List<UserLanguageEntity> languages = userLanguageRepository.findByUser(user);
        List<UserHopeLanguageEntity> hopeLanguages = userHopeLanguageRepository.findByUser(user);
        Long likes = likeRepository.countByUserToId(user.getId());

        UserDetailsDto result = UserDetailsDto.builder()
                .id(user.getId())
                .name(user.getName())
                .imageUrl(user.getImageUrl())
                .content(user.getContent())
                .facebookUrl(user.getFacebookUrl())
                .instagramUrl(user.getInstagramUrl())
                .regDate(user.getRegDate())
                .likes(likes)
                .repCountry(user.getRepCountry())
                .repLanguage(user.getRepLanguage())
                .repHopeLanguage(user.getRepHopeLanguage())
                .country(countrys.stream().map(x -> x.getCountry().getName()).collect(Collectors.toList()))
                .language(languages.stream().map(x -> x.getLanguage().getAbbr()).collect(Collectors.toList()))
                .hopeLanguage(hopeLanguages.stream().map(x -> x.getHopeLanguage().getAbbr()).collect(Collectors.toList()))
                .build();

        return result;
    }

    @Override
    public UserProfileDto findUserProfile(UserEntity user) {

        List<UserCountryEntity> countrys = userCountryRepository.findByUser(user);
        List<UserLanguageEntity> languages = userLanguageRepository.findByUser(user);
        List<UserHopeLanguageEntity> hopeLanguages = userHopeLanguageRepository.findByUser(user);

        UserProfileDto result = UserProfileDto.builder()
                .id(user.getId())
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
                .role(user.getRole())
                .build();

        return result;
    }

    @Override
    @Transactional(readOnly = false)
    @CachePut(value = "userDetail", key = "#updateUserDto.id")
    public UserDetailsDto updateProfile(UpdateUserDto updateUserDto) {

        UserEntity user = userRepository.findById(updateUserDto.getId()).orElseThrow(() -> new UsernameNotFoundException("not found user"));
        Long follows = likeRepository.countByUserToId(user.getId());

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
                .regDate(user.getRegDate())
                .likes(follows)
                .repCountry(user.getRepCountry())
                .repLanguage(user.getRepLanguage())
                .repHopeLanguage(user.getRepHopeLanguage())
                .country(userCountryRepository.findByUser(user).stream().map(x -> x.getCountry().getName()).collect(Collectors.toList()))
                .language(userLanguageRepository.findByUser(user).stream().map(x -> x.getLanguage().getAbbr()).collect(Collectors.toList()))
                .hopeLanguage(userHopeLanguageRepository.findByUser(user).stream().map(x -> x.getHopeLanguage().getAbbr()).collect(Collectors.toList()))
                .build();

        return result;
    }

    @Override
    @Transactional(readOnly = false)
    public String uploadProfileImage(MultipartFile file, Long userId) {
        try {

            UserEntity user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("not found user"));
            String uuid = UUID.randomUUID().toString();
            String fileName = file.getOriginalFilename();
            String saveFileName = uuid + "_" + fileName;
            String extension = fileName.substring(fileName.lastIndexOf(".") + 1);

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("image/" + extension);

            amazonS3.putObject(new PutObjectRequest(bucket, saveFileName, file.getInputStream(), metadata).withCannedAcl(
                    CannedAccessControlList.PublicRead
            ));

            String[] splitUrl = user.getImageUrl().split("/");
            String userImageUrl = splitUrl[splitUrl.length - 1];

            if (!userImageUrl.equals(defaultImage)) {
                amazonS3.deleteObject(new DeleteObjectRequest(bucket, userImageUrl));
            }

            user.changeImageUrl(cloudFrontDomain + saveFileName);

            return user.getImageUrl();

        } catch (Exception e) {
            return "";
        }
    }

    @Override
    @Transactional(readOnly = false)
    public String deleteProfileImage(String imageUrl, Long userId) {
        try {
            UserEntity user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("not found user"));

            String[] splitUrl = user.getImageUrl().split("/");
            String userImageUrl = splitUrl[splitUrl.length - 1];

            if (!userImageUrl.equals(defaultImage)) {
                user.changeImageUrl(cloudFrontDomain + defaultImage);
                amazonS3.deleteObject(new DeleteObjectRequest(bucket, userImageUrl));
                return user.getImageUrl();
            } else{
                return "";
            }
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    public void deleteAccessToken(Long userId) {
        tokenRedisRepository.delete(userId);
    }

    @Override
    @Transactional(readOnly = false)
    public void deleteRefreshToken(Long userId) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("not found user"));
        user.changeRefreshToken("");
    }

    @Override
    @Transactional(readOnly = false)
    public void saveRefreshToken(Long userId, String refreshToken) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("not found user"));

        user.changeRefreshToken(refreshToken);
    }

    @Override
    public String verificationRefreshToken(String refreshToken) {

        Long userId = jwtTokenProvider.getClaimByTokenAndKey(refreshToken, "userId").asLong();
        String refreshKey = jwtTokenProvider.getClaimByTokenAndKey(refreshToken, "refreshKey").asString();

        if (userId == null) {
            return "";
        }

        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("not found user"));

        if (user.getRefreshToken().equals(refreshKey)) {
            String token = jwtTokenProvider.createToken(user.getId());
            tokenRedisRepository.save(user.getId(), token);
            return token;
        } else {
            throw new ConnectElseWhereException();
        }

    }

    @Override
    public Boolean isUser(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    @Override
    public Long getViews(Long userId) {

        Long views = userRedisRepository.findViewsByUserId(userId);

        if (views == null) {
            views = userRepository.findViewsByUserId(userId);
            userRedisRepository.updateViews(userId, views);
        }
        return views;
    }

    @Override
    @Transactional(readOnly = false)
    public void updateViews() {

        Set<String> keys = userRedisRepository.findViewsKeys();
        userRepository.findAll();

        List<UserEntity> users = keys.stream().map(x -> x.split("::")[1]).map(x -> userRepository.findById(Long.parseLong(x)).get())
                .collect(Collectors.toList());

        for (UserEntity user : users) {
            Long views = userRedisRepository.findViewsByUserId(user.getId());
            if (!user.getViews().equals(views)) {
                user.changeViews(views);
            }
        }
    }

    @Override
    @Transactional(readOnly = false)
    public void checkView(Long userFromId, Long userToId) {

        Long views = userRedisRepository.findViewsByUserId(userToId);

        if (userRedisRepository.findViewDuplexByUserIds(userFromId, userToId)) {
            userRedisRepository.updateViewDuplex(userFromId, userToId);
            if (views == null) {
                views = userRepository.findViewsByUserId(userToId);
            }
            userRedisRepository.updateViews(userToId, views + 1);
        }
    }

    @Override
    public void validMyself(Long userFromId, Long userToId) {
        if (userFromId.equals(userToId))
            throw new PointMyselfException();
    }
}
