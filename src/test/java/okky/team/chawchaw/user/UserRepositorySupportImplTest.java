package okky.team.chawchaw.user;

import okky.team.chawchaw.user.dto.FindUserVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("dev")
@Transactional
class UserRepositorySupportImplTest {

    @Autowired
    UserRepository userRepository;

    @Test
    public void 카드조회_동적쿼리() throws Exception {
        //when
        /* 기본, 랜덤순 */
        System.out.println("기본, 랜덤순");
        userRepository.findAllByElement(
                FindUserVo.builder().build());
        /* 2페이지, 랜덤순 */
        System.out.println("2페이지, 랜덤순");
        userRepository.findAllByElement(
                FindUserVo.builder().build());
        /* 언어 검색, 랜덤순 */
        System.out.println("언어 검색, 랜덤순");
        userRepository.findAllByElement(
                FindUserVo.builder().language("yi").build());
        /* 희망 언어 검색, 랜덤순 */
        System.out.println("희망 언어 검색, 랜덤순");
        userRepository.findAllByElement(
                FindUserVo.builder().hopeLanguage("af").build());
        /* 언어, 희망 언어 검색, 랜덤순 */
        System.out.println("언어, 희망 언어 검색, 랜덤순");
        userRepository.findAllByElement(
                FindUserVo.builder().language("yi").hopeLanguage("af").build());
        /* 언어, 희망 언어 검색, 팔로우 순 */
        System.out.println("언어, 희망 언어 검색, 팔로우 순");
        userRepository.findAllByElement(
                FindUserVo.builder().language("yi").hopeLanguage("af").sort("like").build());
        /* 언어, 희망 언어 검색, 조회수 순 */
        System.out.println("언어, 희망 언어 검색, 조회수 순");
        userRepository.findAllByElement(
                FindUserVo.builder().language("yi").hopeLanguage("af").sort("view").build());
        /* 언어, 희망 언어 검색, 날짜 순 */
        System.out.println("언어, 희망 언어 검색, 날짜 순");
        userRepository.findAllByElement(
                FindUserVo.builder().language("yi").hopeLanguage("af").sort("date").build());
        /* 언어, 희망 언어, 이름 검색, 랜덤순 */
        System.out.println("언어, 희망 언어, 이름 검색, 랜덤순");
        userRepository.findAllByElement(
                FindUserVo.builder().language("yi").hopeLanguage("af").name("하주현").build());
        /* 언어, 희망 언어, 이름 검색, 팔로우 순 */
        System.out.println("언어, 희망 언어, 이름 검색, 팔로우 순");
        userRepository.findAllByElement(
                FindUserVo.builder().language("yi").hopeLanguage("af").name("하주현").sort("like").build());
    }

}