package ru.practicum.event.repository;

import org.springframework.lang.Nullable;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.user.model.User;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

public interface CustomEventRepository {

    /**
     * Получение событий по заданным условиям
     * @param userIds Список пользователей, чьи события нужно найти (null - неважно)
     * @param stateIds Список состояний в которых находятся искомые события (null - неважно)
     * @param categoryIds Список id категорий в которых будет вестись поиск (null - неважно)
     * @param rangeStart Дата и время не раньше которых должно произойти событие (null - неважно)
     * @param rangeEnd Дата и время не позже которых должно произойти событие (null - неважно)
     * @param from Количество событий, которые нужно пропустить для формирования текущего набора
     * @param size Количество событий в наборе
     * @return Список полных DTO событий
     */
    @NotNull List<EventFullDto> getEvents(@Nullable List<Long> userIds,
                                          @Nullable List<String> stateIds,
                                          @Nullable List<Long> categoryIds,
                                          @Nullable LocalDateTime rangeStart,
                                          @Nullable LocalDateTime rangeEnd,
                                          int from,
                                          int size);

    /**
     * Получение событий их инициатором
     * @param initiator Идентификатор пользователя
     * @param from Количество элементов, которые нужно пропустить для формирования текущего набора
     * @param size Количество элементов в наборе
     * @return Список полных DTO событий
     */
    @NotNull List<EventFullDto> getEvents(@NotNull User initiator,
                                          int from,
                                          int size);


    /**
     * Получение опубликованных событий по заданным условиям.
     * Если в запросе не указан диапазон дат [rangeStart-rangeEnd],
     * то нужно выгружать события, которые произойдут позже текущей даты и времени.
     * @param text Текст для поиска без учета регистра в содержимом аннотации и подробном описании события (null - неважно)
     * @param categoryIds Список идентификаторов категорий в которых будет вестись поиск (null - неважно)
     * @param paid Поиск только платных/бесплатных событий (null - неважно)
     * @param rangeStart Дата и время не раньше которых должно произойти событие (null - неважно)
     * @param rangeEnd Дата и время не позже которых должно произойти событие (null - неважно)
     * @param onlyAvailable Только события у которых не исчерпан лимит запросов на участие (null - неважно)
     * @param sort Вариант сортировки: по дате события или по количеству просмотров {@link ru.practicum.event.model.EventSort}  (null - неважно)
     * @param from Количество событий, которые нужно пропустить для формирования текущего набора
     * @param size Количество событий в наборе
     * @return Список коротких DTO событий
     */
    @NotNull List<EventShortDto> getEvents(@Nullable String text,
                                           @Nullable List<Long> categoryIds,
                                           @Nullable Boolean paid,
                                           @Nullable LocalDateTime rangeStart,
                                           @Nullable LocalDateTime rangeEnd,
                                           @Nullable Boolean onlyAvailable,
                                           @Nullable String sort,
                                           int from,
                                           int size);

}
