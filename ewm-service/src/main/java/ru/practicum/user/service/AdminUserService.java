package ru.practicum.user.service;

import org.springframework.lang.Nullable;
import ru.practicum.user.dto.NewUserRequest;
import ru.practicum.user.dto.UserDto;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface AdminUserService {

    /**
     * Возвращает информацию обо всех пользователях (учитываются параметры ограничения выборки),
     * либо о конкретных (учитываются указанные идентификаторы)
     * @param ids Идентификаторы пользователей
     * @param from Количество элементов, которые нужно пропустить для формирования текущего набора
     * @param size Количество элементов в наборе
     * @return Список пользователей
     */
    @NotNull List<UserDto> getUsers(@Nullable List<Long> ids, int from, int size);

    /**
     * Добавление пользователя
     * @param newUserRequest Данные пользователя
     * @return Информация о пользователе
     */
    @NotNull UserDto postUser(@NotNull NewUserRequest newUserRequest);

    /**
     * Удаление пользователя
     * @param userId Идентификатор пользователя
     */
    void deleteUser(long userId);

}
