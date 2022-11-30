package ru.practicum.user.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.common.DataRange;
import ru.practicum.common.CommonRepository;
import ru.practicum.exception.AlreadyExistsException;
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
    CommonRepository commonRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public List<UserDto> getUsers(List<Long> ids, int from, int size) {
        DataRange<User> dataRange = new DataRange<>(from, size, Sort.by(Sort.Direction.ASC, "id"));
        List<User> users = userRepository.findAll(dataRange.getPageable()).getContent();
        return dataRange.trimPage(users).stream()
                .filter(user -> (ids.size() == 0 || ids.contains(user.getId())))
                .map(user -> modelMapper.map(user, UserDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserDto postUser(NewUserRequest newUserRequest) {
        if (userRepository.findByEmail(newUserRequest.getEmail()).isPresent()) {
            throw new AlreadyExistsException("user is already exists");
        }
        User user = modelMapper.map(newUserRequest, User.class);
        userRepository.save(user);
        userRepository.flush();
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public void deleteUser(long userId) {
        User user = commonRepository.getUser(userId);
        userRepository.delete(user);
        userRepository.flush();
    }
}
