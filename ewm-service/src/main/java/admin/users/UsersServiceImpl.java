package admin.users;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsersServiceImpl implements UsersService {

    @Override
    public List<UserDto> getUsers(List<Integer> ids, int from, int size) {
        return null;
    }

    @Override
    public UserDto postUser(NewUserRequest newUserRequest) {
        return null;
    }

    @Override
    public void deleteUser(int id) {

    }
}
