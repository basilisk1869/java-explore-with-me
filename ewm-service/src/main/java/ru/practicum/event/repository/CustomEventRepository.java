package ru.practicum.event.repository;

import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

public interface CustomEventRepository {

    List<EventFullDto> getEvents(List<Long> userIds, List<String> stateIds, List<Long> categoryIds,
                                 LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size);

    List<EventFullDto> getEvents(User initiator, Integer from, Integer size);

    List<EventShortDto> getEvents(String text, List<Long> categoryIds, Boolean paid,
                                  LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable,
                                  String sort, Integer from, Integer size);

}
