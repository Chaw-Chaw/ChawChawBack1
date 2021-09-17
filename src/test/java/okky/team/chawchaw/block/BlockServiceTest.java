package okky.team.chawchaw.block;

import okky.team.chawchaw.block.dto.CreateBlockDto;
import okky.team.chawchaw.block.exception.ExistBlockException;
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

    @BeforeEach
    void initEach() {
        for (int i = 0; i < 2; i++) {
            userRepository.save(UserEntity.builder()
                    .email("test" + i + "@test.kr")
                    .password("1234")
                    .name("이름")
                    .web_email("웹메일")
                    .school("학교")
                    .build());
        }
    }

    @Test
    @DisplayName("쌍방 차단 성공")
    public void create_block() throws Exception {
        //when
        Long blockId = blockService.createBlock(new CreateBlockDto(1L, 2L));
        Long blockId2 = blockService.createBlock(new CreateBlockDto(2L, 1L));
        //then
        Assertions.assertThat(blockId).isNotNull();
        Assertions.assertThat(blockId2).isNotNull();
        Assertions.assertThat(blockRepository.findAll().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("쌍방 차단 실패")
    public void create_block_fail() throws Exception {
        //when, then
        Assertions.assertThatExceptionOfType(UsernameNotFoundException.class).isThrownBy(() -> {
            blockService.createBlock(new CreateBlockDto(-1L, 2L));
        });
        Assertions.assertThatExceptionOfType(UsernameNotFoundException.class).isThrownBy(() -> {
            blockService.createBlock(new CreateBlockDto(2L, -1L));
        });
    }

    @Test
    @DisplayName("중복 차단 시 거부")
    public void duplicate_block() throws Exception {
        //when, then
        Assertions.assertThatExceptionOfType(ExistBlockException.class).isThrownBy(() -> {
                    blockService.createBlock(new CreateBlockDto(1L, 2L));
                    blockService.createBlock(new CreateBlockDto(1L, 2L));
                }
        );
    }

}