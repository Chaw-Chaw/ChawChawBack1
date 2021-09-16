package okky.team.chawchaw.chat;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import okky.team.chawchaw.chat.dto.ChatDto;
import okky.team.chawchaw.chat.dto.ChatMessageDto;
import okky.team.chawchaw.chat.dto.ChatRoomDto;
import okky.team.chawchaw.chat.room.ChatRoomEntity;
import okky.team.chawchaw.chat.room.ChatRoomRepository;
import okky.team.chawchaw.chat.room.ChatRoomUserEntity;
import okky.team.chawchaw.chat.room.ChatRoomUserRepository;
import okky.team.chawchaw.user.UserEntity;
import okky.team.chawchaw.user.UserRepository;
import okky.team.chawchaw.utils.exception.NotExistRoomException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
    public ChatRoomDto createRoom(Long userFrom, Long userTo) {
        ChatRoomEntity room = chatRoomRepository.save(new ChatRoomEntity(UUID.randomUUID().toString()));
        UserEntity user = userRepository.findById(userFrom).orElseThrow(() -> new UsernameNotFoundException("not found user"));
        UserEntity user2 = userRepository.findById(userTo).orElseThrow(() -> new UsernameNotFoundException("not found user"));
        chatRoomUserRepository.save(new ChatRoomUserEntity(room, user));
        chatRoomUserRepository.save(new ChatRoomUserEntity(room, user2));
        return new ChatRoomDto(room.getId(), "none");
    }

    @Override
    @Transactional(readOnly = false)
    @CacheEvict(value = "roomUserIds", key = "#roomId")
    public void deleteRoomByRoomIdAndUserId(Long roomId, Long userId) {
        /*
        * 최근 메시지와 방 유저의 퇴장 시간을 비교
        * 최근 메시지 등록일 < 방 유저 퇴장 시간 : 해당 유저가 퇴장 했다는 의미
        * 최근 메시지 등록일 > 방 유저 퇴장 시간 : 해당 유저가 퇴장 하지 않았다는 의미
        * 최근 메시지가 없다면, 방을 생성하고 메시지를 보내지 않았다는 의미
        * */
        LocalDateTime latestMessageRegDate = chatMessageRepository.findAllByRoomIdOrderByRegDateDesc(roomId).stream().map(ChatMessageDto::getRegDate).max(LocalDateTime::compareTo).orElse(null);
        List<ChatRoomUserEntity> roomUsers = chatRoomUserRepository.findAllByChatRoomId(roomId);
        if (latestMessageRegDate == null) {
            if (!roomUsers.stream().map(x -> x.getUser().getId()).collect(Collectors.toList()).contains(userId))
                throw new NotExistRoomException();
            chatRoomRepository.deleteById(roomId);
            chatMessageRepository.deleteByRoomId(roomId);
            return;
        }
        List<ChatRoomUserEntity> existUsers = roomUsers.stream().filter(x -> x.getExitDate().isBefore(latestMessageRegDate)).collect(Collectors.toList());

        if (existUsers.size() < 2) {
            if (!existUsers.get(0).getUser().getId().equals(userId))
                throw new NotExistRoomException();
            chatRoomRepository.deleteById(roomId);
            chatMessageRepository.deleteByRoomId(roomId);
        }
        else {
            existUsers.stream().filter(x -> x.getUser().getId().equals(userId)).forEach(ChatRoomUserEntity::exitRoom);
        }
    }

    @Override
    @Transactional(readOnly = false)
    public List<ChatDto> findMessagesByUserId(Long userId) {
        List<ChatDto> result = new ArrayList<>();
        List<ChatRoomUserEntity> roomUsers = chatRoomUserRepository.findAllByUserId(userId);
        for (ChatRoomUserEntity roomUser : roomUsers) {
            ChatDto chatDto = new ChatDto(roomUser.getChatRoom().getId(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), null);

            List<ChatMessageDto> chatMessages = chatMessageRepository.findAllByRoomIdOrderByRegDateDesc(roomUser.getChatRoom().getId());
            List<ChatMessageDto> chatMessagesDesc = chatMessages.stream().filter(x -> x.getRegDate().isAfter(roomUser.getExitDate())).collect(Collectors.toList());
            /* 내 아이디라면 && 내가 나간 방이라면(메시지가 없다면 나간 방으로 간주) && 자의로 나간지 체크*/
            if (roomUser.getUser().getId().equals(userId) && chatMessagesDesc.isEmpty() && roomUser.getIsExit())
                break;
            chatDto.setMessages(chatMessagesDesc);

            for (ChatRoomUserEntity user : chatRoomUserRepository.findAllByChatRoomId(roomUser.getChatRoom().getId())) {
                chatDto.getParticipantNames().add(user.getUser().getName());
                chatDto.getParticipantIds().add(user.getUser().getId());
                chatDto.getParticipantImageUrls().add(user.getUser().getImageUrl());
            }
            result.add(chatDto);
        }

        return result;
    }

    /**
     * 해당 유저가 가지고 있는 메시지 중에서 읽음 처리가 되어있지 않은 메시지를 최신순으로 전달한다.
     * @param userId
     * @return 조건에 맞는 메시지 집합
     */
    @Override
    public List<ChatMessageDto> findMessagesByUserIdAndRegDate(Long userId) {
        List<ChatMessageDto> result = new ArrayList<>();
        List<ChatRoomUserEntity> users = chatRoomUserRepository.findAllByUserId(userId);
        for (ChatRoomUserEntity user : users) {
            for (ChatRoomUserEntity roomUsers : chatRoomUserRepository.findAllByChatRoomId(user.getChatRoom().getId())) {
                if (!roomUsers.getUser().getId().equals(userId)) {
                    result.addAll(chatMessageRepository.findAllByRoomIdAndUserId(user.getChatRoom().getId(), userId));
                }
            }
        }
        result.sort(Comparator.comparing(ChatMessageDto::getRegDate).reversed());
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
    public Boolean updateCurrentRoom(String email, Long roomId, Long userId) {
        try {
            chatMessageRepository.updateSession(email, roomId, userId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Boolean isConnection(String email, Long roomId) {
        return chatMessageRepository.isSession(email, roomId);
    }
}
