package okky.team.chawchaw.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okky.team.chawchaw.user.UserService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ScheduleConfig {

    private final UserService userService;

    @Scheduled(cron = "0 0 2 * * *")
    public void updateUserViews() {
        log.info("조회수 동기화 시작");
        userService.updateViews();
        log.info("조회수 동기화 종료");
    }

}
