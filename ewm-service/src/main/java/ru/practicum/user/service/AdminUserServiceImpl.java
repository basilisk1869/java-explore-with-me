package ru.practicum.user.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.user.dto.NewUserRequest;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.repository.UserRepository;
import ru.practicum.user.service.AdminUserService;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminUserServiceImpl implements AdminUserService {

    @Autowired
    UserRepository userRepository;

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
