package ru.practicum.event.service;

import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.request.dto.ParticipationRequestDto;

import java.util.List;

public interface UserEventService {

    List<EventFullDto> getEvents(long userId, Integer from, Integer size);

    EventFullDto patchEvent(long userId, NewEventDto newEventDto);

    EventFullDto postEvent(long userId, NewEventDto newEventDto);

    EventFullDto getEvent(long userId, long eventId);

    EventFullDto cancelEvent(long userId, long eventId);

    List<ParticipationRequestDto> getRequests(long userId, long eventId);

    ParticipationRequestDto confirmRequest(long userId, long eventId, long reqId);

    ParticipationRequestDto rejectRequest(long userId, long eventId, long reqId);

}
