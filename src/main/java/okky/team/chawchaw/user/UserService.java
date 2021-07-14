package okky.team.chawchaw.user;

import okky.team.chawchaw.user.dto.UserDto;

public interface UserService {

    public UserDto createUser(UserDto userDto);

    public void deleteUser(String email);
}
