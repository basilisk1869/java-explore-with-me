package ru.practicum.event.service;

import ru.practicum.event.dto.EventFullDto;

import java.time.LocalDateTime;
import java.util.List;

public interface PublicEventService {

    List<EventFullDto> getEvents(String text, List<Integer> categories, Boolean paid,
                                 LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                 Boolean onlyAvailable, String sort, Integer from, Integer size);

    EventFullDto getEvent(long eventId);

}
