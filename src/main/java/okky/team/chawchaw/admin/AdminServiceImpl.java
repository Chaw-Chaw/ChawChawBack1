package okky.team.chawchaw.admin;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import okky.team.chawchaw.admin.dto.*;
import okky.team.chawchaw.block.BlockEntity;
import okky.team.chawchaw.block.BlockRedisRepository;
import okky.team.chawchaw.block.BlockRepository;
import okky.team.chawchaw.block.dto.BlockSessionDto;
import okky.team.chawchaw.block.exception.ExistBlockException;
import okky.team.chawchaw.block.exception.NotExistBlockException;
import okky.team.chawchaw.like.LikeRepository;
import okky.team.chawchaw.user.Role;
import okky.team.chawchaw.user.UserEntity;
import okky.team.chawchaw.user.UserRepository;
import okky.team.chawchaw.user.country.CountryRepository;
import okky.team.chawchaw.user.country.UserCountryEntity;
import okky.team.chawchaw.user.country.UserCountryRepository;
import okky.team.chawchaw.user.dto.UserCardDto;
import okky.team.chawchaw.user.dto.UserDetailsDto;
import okky.team.chawchaw.user.language.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminServiceImpl implements AdminService{

    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final CountryRepository countryRepository;
    private final UserCountryRepository userCountryRepository;
    private final LanguageRepository languageRepository;
    private final UserLanguageRepository userLanguageRepository;
    private final UserHopeLanguageRepository userHopeLanguageRepository;
    private final BlockRepository blockRepository;
    private final BlockRedisRepository blockRedisRepository;
    private final AmazonS3 amazonS3;
    @Value("${user.profile.image.default}")
    private String defaultImage;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    @Value("${cloud.front.domain}")
    private String cloudFrontDomain;

    @Override
    public List<UserCardDto> findUsers(FindUserDto findUserDto) {
        return null;
    }

    @Override
    public UserDetailDto findUserDetail(Long userId) {
        return null;
    }

    @Override
    @Transactional(readOnly = false)
    @CachePut(value = "userDetail", key = "#updateProfileDto.userId")
    public UserDetailsDto updateProfile(UpdateProfileDto updateProfileDto) {

        UserEntity user = userRepository.findById(updateProfileDto.getUserId()).orElseThrow(() -> new UsernameNotFoundException("not found user"));
        Long follows = likeRepository.countByUserToId(user.getId());

        if (user.getRole().equals(Role.GUEST)) {
            user.changeRole(Role.USER);
        }
        user.changeContent(updateProfileDto.getContent());
        user.changeFacebookUrl(updateProfileDto.getFacebookUrl());
        user.changeInstagramUrl(updateProfileDto.getInstagramUrl());
        user.changeRepCountry(updateProfileDto.getRepCountry());
        user.changeRepLanguage(updateProfileDto.getRepLanguage());
        user.changeRepHopeLanguage(updateProfileDto.getRepHopeLanguage());

        List<UserCountryEntity> userCountrys = userCountryRepository.findByUser(user);
        List<UserLanguageEntity> userLanguages = userLanguageRepository.findByUser(user);
        List<UserHopeLanguageEntity> userHopeLanguages = userHopeLanguageRepository.findByUser(user);

        Set<String> addCountrys = updateProfileDto.getCountry();
        Set<String> removeCountrys = userCountrys.stream().map(x -> x.getCountry().getName()).collect(Collectors.toSet());

        Set<String> addLanguages = updateProfileDto.getLanguage();
        Set<String> removeLanguages = userLanguages.stream().map(x -> x.getLanguage().getAbbr()).collect(Collectors.toSet());

        Set<String> addHopeLanguages = updateProfileDto.getHopeLanguage();
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
    @CacheEvict(value = "userDetail", key = "#userId")
    public void deleteUser(Long userId) {

        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("not found user"));
        likeRepository.deleteByUserFromOrUserTo(user, user);
        blockRepository.deleteByUserFromOrUserTo(user, user);
        if (user != null)
            userRepository.delete(user);
    }

    @Override
    @Transactional(readOnly = false)
    public void createBlock(CreateBlockDto createBlockDto) {

        if (!blockRepository.existsByUserFromIdAndUserToId(createBlockDto.getUserFromId(), createBlockDto.getUserToId())) {
            blockRepository.save(new BlockEntity(new UserEntity(createBlockDto.getUserFromId()), new UserEntity(createBlockDto.getUserToId())));
            updateBlockSession(createBlockDto.getUserFromEmail());
        }
        else {
            throw new ExistBlockException();
        }
    }

    @Override
    @Transactional(readOnly = false)
    public void deleteBlock(DeleteBlockDto deleteBlockDto) {

        if (blockRepository.existsByUserFromIdAndUserToId(deleteBlockDto.getUserFromId(), deleteBlockDto.getUserToId())) {
            blockRepository.deleteByUserFromIdAndUserToId(deleteBlockDto.getUserFromId(), deleteBlockDto.getUserToId());
            updateBlockSession(deleteBlockDto.getUserFromEmail());
        }
        else {
            throw new NotExistBlockException();
        }
    }

    @Override
    public void updateBlockSession(String email) {

        if (blockRedisRepository.isBlock(email)) {

            List<BlockSessionDto> blockUsers = new ArrayList<>();

            blockRepository.findSessionDtoByUserFromEmail(email).stream().filter(x -> !blockUsers.contains(x.getUserId())).forEach(blockUsers::add);
            blockRepository.findSessionDtoByUserToEmail(email).stream().filter(x -> !blockUsers.contains(x.getUserId())).forEach(blockUsers::add);

            blockRedisRepository.update(blockUsers, email);
        }
    }

    @Override
    @Transactional(readOnly = false)
    public String updateProfileImage(UploadProfileImageDto profileImageDto) {

        try {
            UserEntity user = userRepository.findById(profileImageDto.getUserId()).orElseThrow(() -> new UsernameNotFoundException("not found user"));
            String uuid = UUID.randomUUID().toString();
            String fileName = profileImageDto.getFile().getOriginalFilename();
            String saveFileName = uuid + "_" + fileName;
            String extension = fileName.substring(fileName.lastIndexOf(".") + 1);

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("image/" + extension);

            amazonS3.putObject(new PutObjectRequest(bucket, saveFileName, profileImageDto.getFile().getInputStream(), metadata).withCannedAcl(
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
    public String deleteProfileImage(DeleteProfileImageDto profileImageDto) {

        try {
            UserEntity user = userRepository.findById(profileImageDto.getUserId()).orElseThrow(() -> new UsernameNotFoundException("not found user"));

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

}
