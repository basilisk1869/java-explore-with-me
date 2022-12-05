package ru.practicum.user.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.common.repository.CommonRepository;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

@Service
public class UserUserServiceImpl implements UserUserService {

    @Autowired
    CommonRepository commonRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public UserDto showRating(long userId, boolean showRating) {
        User user = commonRepository.getUser(userId);
        user.setShowRating(showRating);
        userRepository.save(user);
        return modelMapper.map(user, UserDto.class);
    }

}
