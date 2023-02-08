package ru.practicum.user.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.common.repository.CommonRepository;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import javax.validation.constraints.NotNull;

@Service
@RequiredArgsConstructor
public class UserUserServiceImpl implements UserUserService {

    @Autowired
    private final CommonRepository commonRepository;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final ModelMapper modelMapper;

    @Override
    public @NotNull UserDto showRating(long userId, boolean showRating) {
        User user = commonRepository.getUser(userId);
        user.setShowRating(showRating);
        userRepository.save(user);
        return modelMapper.map(user, UserDto.class);
    }

}
