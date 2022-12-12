package ru.practicum.user.service;

import ru.practicum.user.dto.UserDto;

import javax.validation.constraints.NotNull;

public interface UserUserService {

    /**
     * Показать/скрыть свой рейтинг инициатора событий и рейтинги своих событий
     * @param userId Идентификатор пользователя
     * @param showRating Показывать ли рейтинг
     * @return Обновленные данные пользователя
     */
    @NotNull UserDto showRating(long userId, boolean showRating);

}
