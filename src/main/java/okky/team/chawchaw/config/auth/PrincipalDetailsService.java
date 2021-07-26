package okky.team.chawchaw.config.auth;

import lombok.RequiredArgsConstructor;
import okky.team.chawchaw.user.UserEntity;
import okky.team.chawchaw.user.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("유저를 찾지 못함"));
        if (user == null)
            throw new UsernameNotFoundException("not found email");
        else
            return new PrincipalDetails(user);
    }
}
