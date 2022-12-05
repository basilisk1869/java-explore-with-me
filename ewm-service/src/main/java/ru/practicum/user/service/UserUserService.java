package ru.practicum.user.service;

import ru.practicum.user.dto.UserDto;

public interface UserUserService {

    UserDto showRating(long userId, boolean showRating);

}
