package ru.practicum.event.service;

import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.NewEventDto;

import java.time.LocalDateTime;
import java.util.List;

public interface AdminEventService {

    List<EventFullDto> getEvents(List<Long> users, List<String> states, List<Long> categories,
                                 LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                 Integer from, Integer size);

    EventFullDto putEvent(long eventId, NewEventDto newEventDto);

    EventFullDto publishEvent(long eventId);

    EventFullDto rejectEvent(long eventId);

}
