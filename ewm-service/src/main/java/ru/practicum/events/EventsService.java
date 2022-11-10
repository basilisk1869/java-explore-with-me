package ru.practicum.events;

import java.time.LocalDateTime;
import java.util.List;

public interface EventsService {

    List<EventFullDto> getEvents(List<Integer> users, List<String> states, List<Integer> categories,
                                 LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                 Integer from, Integer size);

    EventFullDto putEvent(long eventId, NewEventDto newEventDto);

    EventFullDto publishEvent(long eventId);

    EventFullDto rejectEvent(long eventId);

}
