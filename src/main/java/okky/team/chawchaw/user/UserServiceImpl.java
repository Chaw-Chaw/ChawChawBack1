package okky.team.chawchaw.user;

import lombok.RequiredArgsConstructor;
import okky.team.chawchaw.user.dto.UserDto;
import okky.team.chawchaw.utils.DtoToEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService{

    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = false)
    public UserDto createUser(UserDto userDto) {
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        UserEntity user = DtoToEntity.userDto(userDto);
        userRepository.save(user);
        return userDto;
    }

    @Override
    @Transactional(readOnly = false)
    public void deleteUser(String email) {
        List<UserEntity> users = userRepository.findByEmail(email);
        UserEntity user = users.get(0);
        if (user != null){
            userRepository.delete(user);
        }
    }

}
