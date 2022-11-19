package ru.practicum.user.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.common.DataRange;
import ru.practicum.exception.NotFoundException;
import ru.practicum.user.dto.NewUserRequest;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminUserServiceImpl implements AdminUserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public List<UserDto> getUsers(List<Long> ids, int from, int size) {
        DataRange<User> dataRange = new DataRange<>(from, size, Sort.by(Sort.Direction.ASC, "id"));
        return userRepository.findAll(dataRange.getPageable()).stream()
                .filter(user -> (ids.size() == 0 || ids.contains(user.getId())))
                .map(user -> modelMapper.map(user, UserDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserDto postUser(NewUserRequest newUserRequest) {
        User user = modelMapper.map(newUserRequest, User.class);
        userRepository.save(user);
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public void deleteUser(long userId) {
        User user = getUser(userId);
        userRepository.delete(user);
    }

    private User getUser(long userId) {
        return userRepository.findById(userId).orElseThrow(() -> {
            throw new NotFoundException("user is not found");
        });
    }
}
