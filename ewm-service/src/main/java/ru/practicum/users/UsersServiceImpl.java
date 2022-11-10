package ru.practicum.users;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UsersServiceImpl implements UsersService {

    @Autowired
    UsersRepository usersRepository;

    @Override
    public List<UserDto> getUsers(List<Integer> ids, int from, int size) {
        return null;
    }

    @Override
    public UserDto postUser(NewUserRequest newUserRequest) {
        return null;
    }

    @Override
    public void deleteUser(long userId) {

    }
}
