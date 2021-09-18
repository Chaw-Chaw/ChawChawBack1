package okky.team.chawchaw.block;

import okky.team.chawchaw.block.dto.BlockUserDto;
import okky.team.chawchaw.block.dto.CreateBlockDto;
import okky.team.chawchaw.block.dto.DeleteBlockDto;
import okky.team.chawchaw.block.exception.ExistBlockException;
import okky.team.chawchaw.block.exception.NotExistBlockException;
import okky.team.chawchaw.user.UserEntity;
import okky.team.chawchaw.user.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
@Transactional
@ActiveProfiles("dev")
class BlockServiceTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BlockService blockService;
    @Autowired
    private BlockRepository blockRepository;
    private List<UserEntity> users;

    @BeforeEach
    void initEach() {
        users = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            UserEntity user = userRepository.save(UserEntity.builder()
                    .email("test" + i + "@test.kr")
                    .password("1234")
                    .name("이름")
                    .web_email("웹메일")
                    .school("학교")
                    .imageUrl("이미지주소")
                    .build());
            users.add(user);
        }
    }

    @Test
    @DisplayName("잘못된 아이디 번호로 인한 차단 실패")
    public void create_block_fail() throws Exception {
        //when, then
        Assertions.assertThatExceptionOfType(UsernameNotFoundException.class).isThrownBy(() -> {
            blockService.createBlock(new CreateBlockDto(users.get(0).getId(), -1L));
        });
    }

    @Test
    @DisplayName("쌍방 차단 성공")
    public void create_block() throws Exception {
        //when
        Long blockId = blockService.createBlock(new CreateBlockDto(users.get(0).getId(), users.get(1).getId()));
        Long blockId2 = blockService.createBlock(new CreateBlockDto(users.get(1).getId(), users.get(0).getId()));
        //then
        Assertions.assertThat(blockId).isNotNull();
        Assertions.assertThat(blockId2).isNotNull();
        Assertions.assertThat(blockRepository.findAll().size()).isEqualTo(2);
    }


    @Test
    @DisplayName("중복 차단을 하려고 했을 경우")
    public void duplicate_block() throws Exception {
        //when, then
        Assertions.assertThatExceptionOfType(ExistBlockException.class).isThrownBy(() -> {
                    blockService.createBlock(new CreateBlockDto(users.get(0).getId(), users.get(1).getId()));
                    blockService.createBlock(new CreateBlockDto(users.get(0).getId(), users.get(1).getId()));
                }
        );
    }

    @Test
    @DisplayName("잘못된 아이디 번호로 인한 차단 해제 실패")
    public void delete_block_fail() throws Exception {
        //when, then
        Assertions.assertThatExceptionOfType(NotExistBlockException.class).isThrownBy(() -> {
            blockService.deleteBlock(new DeleteBlockDto(users.get(0).getId(), -1L));
        });
    }

    @Test
    @DisplayName("차단 해제 성공")
    public void delete_block() throws Exception {
        //given
        blockRepository.save(new BlockEntity(users.get(0), users.get(1)));
        //when
        blockService.deleteBlock(new DeleteBlockDto(users.get(0).getId(), users.get(1).getId()));
        //then
        Assertions.assertThat(blockRepository.findAll().size()).isEqualTo(0);
    }

    @Test
    @DisplayName("차단하지 않은 대상을 차단 해제하려고 했을 경우")
    public void delete_not_exist_block() throws Exception {
        //when, then
        Assertions.assertThatExceptionOfType(NotExistBlockException.class).isThrownBy(() -> {
            blockService.deleteBlock(new DeleteBlockDto(users.get(0).getId(), users.get(1).getId()));
        });
    }

    @Test
    @DisplayName("차단한 유저 조회")
    public void find_block_users() throws Exception {
        //given
        blockRepository.save(new BlockEntity(users.get(0), users.get(1)));
        blockRepository.save(new BlockEntity(users.get(0), users.get(2)));
        //when
        List<BlockUserDto> blockUsers = blockService.findBlockUsers(users.get(0).getId());
        //then
        Assertions.assertThat(blockUsers.size()).isEqualTo(2);
        Assertions.assertThat(blockUsers)
                .usingRecursiveComparison()
                .isEqualTo(Arrays.asList(
                        new BlockUserDto(users.get(1).getId(), users.get(1).getName(), users.get(1).getImageUrl()),
                        new BlockUserDto(users.get(2).getId(), users.get(2).getName(), users.get(2).getImageUrl())
                ));
    }

}