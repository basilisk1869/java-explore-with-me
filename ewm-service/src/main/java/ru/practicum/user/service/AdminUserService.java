package ru.practicum.user.service;

import ru.practicum.user.dto.NewUserRequest;
import ru.practicum.user.dto.UserDto;

import java.util.List;

public interface AdminUserService {

    List<UserDto> getUsers(List<Long> ids, int from, int size);

    UserDto postUser(NewUserRequest newUserRequest);

    void deleteUser(long userId);

}
