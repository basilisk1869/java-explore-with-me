package ru.practicum.user.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ru.practicum.common.DataRange;
import ru.practicum.common.repository.CommonRepositoryImpl;
import ru.practicum.exception.AlreadyExistsException;
import ru.practicum.user.dto.NewUserRequest;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final CommonRepositoryImpl commonRepository;

    @Autowired
    private final ModelMapper modelMapper;

    @Override
    public @NotNull List<UserDto> getUsers(@Nullable List<Long> ids, int from, int size) {
        DataRange<User> dataRange = new DataRange<>(from, size, Sort.by(Sort.Direction.ASC, "id"));
        List<User> users = userRepository.findAll(dataRange.getPageable()).getContent();
        return dataRange.trimPage(users).stream()
                .filter(user -> (ids.size() == 0 || ids.contains(user.getId())))
                .map(user -> modelMapper.map(user, UserDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public @NotNull UserDto postUser(@NotNull NewUserRequest newUserRequest) {
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
