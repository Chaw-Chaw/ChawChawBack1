package okky.team.chawchaw.chat;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import okky.team.chawchaw.chat.dto.ChatDto;
import okky.team.chawchaw.chat.dto.ChatMessageDto;
import okky.team.chawchaw.chat.room.ChatRoomEntity;
import okky.team.chawchaw.chat.room.ChatRoomRepository;
import okky.team.chawchaw.chat.room.ChatRoomUserEntity;
import okky.team.chawchaw.chat.room.ChatRoomUserRepository;
import okky.team.chawchaw.user.UserEntity;
import okky.team.chawchaw.user.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomUserRepository chatRoomUserRepository;
    private final UserRepository userRepository;
    private final AmazonS3 amazonS3;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    @Value("${cloud.front.domain}")
    private String cloudFrontDomain;


    @Override
    @Transactional(readOnly = false)
    public ChatMessageDto createRoom(Long userFrom, Long userTo) {
        ChatRoomEntity room = chatRoomRepository.save(new ChatRoomEntity(UUID.randomUUID().toString()));
        UserEntity user = userRepository.findById(userFrom).orElseThrow(() -> new UsernameNotFoundException("not found user"));
        UserEntity user2 = userRepository.findById(userTo).orElseThrow(() -> new UsernameNotFoundException("not found user"));
        chatRoomUserRepository.save(new ChatRoomUserEntity(room, user));
        chatRoomUserRepository.save(new ChatRoomUserEntity(room, user2));
        ChatMessageDto message = new ChatMessageDto(MessageType.ENTER, room.getId(), user.getId(), user.getName(), user.getName() + "님이 입장하셨습니다.", user.getImageUrl(), LocalDateTime.now().withNano(0));
        chatMessageRepository.save(message);
        return message;
    }

    @Override
    @Transactional(readOnly = false)
    public void deleteRoom(Long roomId) {
        chatRoomRepository.deleteById(roomId);
        chatMessageRepository.deleteByRoomId(roomId);
    }

    @Override
    @Transactional(readOnly = false)
    public List<ChatDto> findMessagesByUserId(Long userId) {
        List<ChatDto> result = new ArrayList<>();
        List<ChatRoomUserEntity> users = chatRoomUserRepository.findAllByUserId(userId);
        for (ChatRoomUserEntity user : users) {
            for (ChatRoomUserEntity roomUsers : chatRoomUserRepository.findAllByChatRoomId(user.getChatRoom().getId())) {
                if (!roomUsers.getUser().getId().equals(userId)) {
                    result.add(new ChatDto(
                            user.getChatRoom().getId(),
                            roomUsers.getUser().getId(),
                            roomUsers.getUser().getName(),
                            roomUsers.getUser().getImageUrl(),
                            chatMessageRepository.findAllByRoomIdOrderByRegDateAsc(user.getChatRoom().getId())
                    ));
                }
            }
        }
        return result;
    }

    @Override
    public String uploadMessageImage(MultipartFile file) {
        try {

            String fileName = file.getOriginalFilename();
            String saveFileName = UUID.randomUUID().toString() + "_" + fileName;
            String extension = fileName.substring(fileName.lastIndexOf(".") + 1);

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("image/" + extension);

            amazonS3.putObject(new PutObjectRequest(bucket, saveFileName, file.getInputStream(), metadata).withCannedAcl(
                    CannedAccessControlList.PublicRead
            ));

            return cloudFrontDomain + saveFileName;

        } catch (Exception e) {
            return "";
        }
    }

    @Override
    @Transactional(readOnly = false)
    public void sendMessage(ChatMessageDto chatMessageDto) {
        chatMessageRepository.save(chatMessageDto);
    }

    @Override

    public Boolean isRoom(Long userId, Long userId2) {
        Boolean result = chatRoomUserRepository.isChatRoom(userId, userId2);
        return result != null && result;
    }
}
