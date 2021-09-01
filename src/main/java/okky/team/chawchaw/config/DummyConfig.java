package okky.team.chawchaw.config;

import com.google.common.collect.Sets;
import lombok.RequiredArgsConstructor;
import okky.team.chawchaw.user.UserEntity;
import okky.team.chawchaw.user.UserRepository;
import okky.team.chawchaw.user.UserService;
import okky.team.chawchaw.user.country.CountryEntity;
import okky.team.chawchaw.user.country.CountryRepository;
import okky.team.chawchaw.user.dto.UpdateUserDto;
import okky.team.chawchaw.user.language.LanguageEntity;
import okky.team.chawchaw.user.language.LanguageRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class DummyConfig implements CommandLineRunner {

    private final UserRepository userRepository;
    private final UserService userService;
    private final CountryRepository countryRepository;
    private final LanguageRepository languageRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final Environment env;

    @Override
    public void run(String... args) throws Exception {

        Boolean isStart = userService.isUser("test0@naver.com");
        Random rd = new Random();
        List<LanguageEntity> languages = languageRepository.findAll();
        List<CountryEntity> countrys = countryRepository.findAll();

        if (!isStart) {
            for (int i = 0; i < 20; i++) {
                /* 랜덤 언어, 희망 언어, 나라 생성 */
                ArrayList<String> language = new ArrayList();
                ArrayList<String> hopeLanguage = new ArrayList();
                ArrayList<String> country = new ArrayList();
                for (int j = 0; j < rd.nextInt(3) + 1; j++) {
                    language.add(languages.get(rd.nextInt(104)).getAbbr());
                }
                for (int j = 0; j < rd.nextInt(3) + 1; j++) {
                    hopeLanguage.add(languages.get(rd.nextInt(104)).getAbbr());
                }
                for (int j = 0; j < rd.nextInt(3) + 1; j++) {
                    country.add(countrys.get(rd.nextInt(249)).getName());
                }
                /* 아이디 생성 */
                UserEntity user = userRepository.save(UserEntity.builder()
                    .email("test" + i +"@naver.com")
                    .password(passwordEncoder.encode("sssssssS1!"))
                    .name(randomHangulName())
                    .web_email("school@school.ac.kr")
                    .school("서울시립대학교")
                    .imageUrl(
                            env.getProperty("cloud.front.domain") +
                            env.getProperty("user.profile.image.default")
                    )
                    .build());
                /* 프로필 생성 */
                userService.updateProfile(UpdateUserDto.builder()
                        .id(user.getId())
                        .content("내용" + i)
                        .facebookUrl("https://www.facebook.com/")
                        .instagramUrl("https://www.instagram.com/")
                        .repCountry(country.get(0))
                        .repLanguage(language.get(0))
                        .repHopeLanguage(hopeLanguage.get(0))
                        .country(Sets.newHashSet(country))
                        .language(Sets.newHashSet(language))
                        .hopeLanguage(Sets.newHashSet(hopeLanguage))
                        .build());
            }
        }

    }

    public String randomHangulName() {
        List<String> firstName = Arrays.asList("김", "이", "박", "최", "정", "강", "조", "윤", "장", "임", "한", "오", "서", "신", "권", "황", "안",
                "송", "류", "전", "홍", "고", "문", "양", "손", "배", "조", "백", "허", "유", "남", "심", "노", "정", "하", "곽", "성", "차", "주",
                "우", "구", "신", "임", "나", "전", "민", "유", "진", "지", "엄", "채", "원", "천", "방", "공", "강", "현", "함", "변", "염", "양",
                "변", "여", "추", "노", "도", "소", "신", "석", "선", "설", "마", "길", "주", "연", "방", "위", "표", "명", "기", "반", "왕", "금",
                "옥", "육", "인", "맹", "제", "모", "장", "남", "탁", "국", "여", "진", "어", "은", "편", "구", "용");
        List<String> lastName = Arrays.asList("가", "강", "건", "경", "고", "관", "광", "구", "규", "근", "기", "길", "나", "남", "노", "누", "다",
                "단", "달", "담", "대", "덕", "도", "동", "두", "라", "래", "로", "루", "리", "마", "만", "명", "무", "문", "미", "민", "바", "박",
                "백", "범", "별", "병", "보", "빛", "사", "산", "상", "새", "서", "석", "선", "설", "섭", "성", "세", "소", "솔", "수", "숙", "순",
                "숭", "슬", "승", "시", "신", "아", "안", "애", "엄", "여", "연", "영", "예", "오", "옥", "완", "요", "용", "우", "원", "월", "위",
                "유", "윤", "율", "으", "은", "의", "이", "익", "인", "일", "잎", "자", "잔", "장", "재", "전", "정", "제", "조", "종", "주", "준",
                "중", "지", "진", "찬", "창", "채", "천", "철", "초", "춘", "충", "치", "탐", "태", "택", "판", "하", "한", "해", "혁", "현", "형",
                "혜", "호", "홍", "화", "환", "회", "효", "훈", "휘", "희", "운", "모", "배", "부", "림", "봉", "혼", "황", "량", "린", "을", "비",
                "솜", "공", "면", "탁", "온", "디", "항", "후", "려", "균", "묵", "송", "욱", "휴", "언", "령", "섬", "들", "견", "추", "걸", "삼",
                "열", "웅", "분", "변", "양", "출", "타", "흥", "겸", "곤", "번", "식", "란", "더", "손", "술", "훔", "반", "빈", "실", "직", "흠",
                "흔", "악", "람", "뜸", "권", "복", "심", "헌", "엽", "학", "개", "롱", "평", "늘", "늬", "랑", "얀", "향", "울", "련");
        Collections.shuffle(firstName);
        Collections.shuffle(lastName);
        return firstName.get(0) + lastName.get(0) + lastName.get(1);
    }
}
