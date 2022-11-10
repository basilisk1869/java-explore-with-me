package ru.practicum.users;

import java.util.List;

public interface UsersService {

    List<UserDto> getUsers(List<Integer> ids, int from, int size);

    UserDto postUser(NewUserRequest newUserRequest);

    void deleteUser(long userId);

}
