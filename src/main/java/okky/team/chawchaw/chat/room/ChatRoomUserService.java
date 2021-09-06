package okky.team.chawchaw.chat.room;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatRoomUserService {

    private final ChatRoomUserRepository chatRoomUserRepository;

    public List<Long> findUserIdsByChatRoomId(Long roomId) {
        return chatRoomUserRepository.findAllByChatRoomId(roomId).stream().map(x -> x.getUser().getId()).collect(Collectors.toList());
    }

}
