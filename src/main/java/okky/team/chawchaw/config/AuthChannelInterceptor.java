package okky.team.chawchaw.config;

import lombok.RequiredArgsConstructor;
import okky.team.chawchaw.block.BlockService;
import okky.team.chawchaw.chat.ChatMessageRepository;
import okky.team.chawchaw.config.auth.PrincipalDetails;
import okky.team.chawchaw.config.jwt.JwtTokenProvider;
import okky.team.chawchaw.user.Role;
import okky.team.chawchaw.user.UserEntity;
import okky.team.chawchaw.user.UserRepository;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthChannelInterceptor implements ChannelInterceptor {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final BlockService blockService;
    private final ChatMessageRepository chatMessageRepository;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        final StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        StompCommand command = accessor.getCommand();

        if (StompCommand.CONNECT == command) {
            String authorization = accessor.getFirstNativeHeader("Authorization");

            if (authorization == null || !authorization.startsWith("Bearer")) {
                return null;
            } else {
                String token = authorization.replace("Bearer ", "");
                Long userId = jwtTokenProvider.getClaimByTokenAndKey(token, "userId").asLong();
                UserEntity user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("유저를 찾지 못함"));
                if (!user.getRole().equals(Role.USER)) {
                    return null;
                }
                PrincipalDetails principalDetails = new PrincipalDetails(user);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());
                accessor.setUser(authentication);
                chatMessageRepository.createSession(user.getEmail());
                blockService.createSession(user.getEmail());
            }

        } else if (StompCommand.SEND == command) {
        } else if (StompCommand.DISCONNECT == command) {
            if (accessor.getUser() != null) {
                chatMessageRepository.deleteSession(accessor.getUser().getName());
                blockService.deleteSession(accessor.getUser().getName());
            }
        }

        return message;
    }
}

