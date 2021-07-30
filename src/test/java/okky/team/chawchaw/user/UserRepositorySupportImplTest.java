package okky.team.chawchaw.user;

import okky.team.chawchaw.user.dto.FindUserVo;
import okky.team.chawchaw.user.dto.UserCardDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
                FindUserVo.builder().pageNo(1).build());
        /* 2페이지, 랜덤순 */
        System.out.println("2페이지, 랜덤순");
        userRepository.findAllByElement(
                FindUserVo.builder().pageNo(2).build());
        /* 언어 검색, 랜덤순 */
        System.out.println("언어 검색, 랜덤순");
        userRepository.findAllByElement(
                FindUserVo.builder().language("yi").pageNo(1).build());
        /* 희망 언어 검색, 랜덤순 */
        System.out.println("희망 언어 검색, 랜덤순");
        userRepository.findAllByElement(
                FindUserVo.builder().hopeLanguage("af").pageNo(1).build());
        /* 언어, 희망 언어 검색, 랜덤순 */
        System.out.println("언어, 희망 언어 검색, 랜덤순");
        userRepository.findAllByElement(
                FindUserVo.builder().language("yi").hopeLanguage("af").pageNo(1).build());
        /* 언어, 희망 언어 검색, 팔로우 순 */
        System.out.println("언어, 희망 언어 검색, 팔로우 순");
        userRepository.findAllByElement(
                FindUserVo.builder().language("yi").hopeLanguage("af").order("like").pageNo(1).build());
        /* 언어, 희망 언어 검색, 조회수 순 */
        System.out.println("언어, 희망 언어 검색, 조회수 순");
        userRepository.findAllByElement(
                FindUserVo.builder().language("yi").hopeLanguage("af").order("view").pageNo(1).build());
        /* 언어, 희망 언어, 이름 검색, 랜덤순 */
        System.out.println("언어, 희망 언어, 이름 검색, 랜덤순");
        userRepository.findAllByElement(
                FindUserVo.builder().language("yi").hopeLanguage("af").name("하주현").pageNo(1).build());
        /* 언어, 희망 언어, 이름 검색, 팔로우 순 */
        System.out.println("언어, 희망 언어, 이름 검색, 팔로우 순");
        userRepository.findAllByElement(
                FindUserVo.builder().language("yi").hopeLanguage("af").name("하주현").order("like").pageNo(1).build());
    }

}