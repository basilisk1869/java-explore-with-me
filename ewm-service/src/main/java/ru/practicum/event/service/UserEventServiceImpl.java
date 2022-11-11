package ru.practicum.event.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.request.dto.ParticipationRequestDto;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserEventServiceImpl implements UserEventService {

    @Autowired
    EventRepository eventRepository;

    @Override
    public List<EventFullDto> getEvents(long userId, Integer from, Integer size) {
        return null;
    }

    @Override
    public EventFullDto patchEvent(long userId, NewEventDto newEventDto) {
        return null;
    }

    @Override
    public EventFullDto postEvent(long userId, NewEventDto newEventDto) {
        return null;
    }

    @Override
    public EventFullDto getEvent(long userId, long eventId) {
        return null;
    }

    @Override
    public EventFullDto cancelEvent(long userId, long eventId) {
        return null;
    }

    @Override
    public List<ParticipationRequestDto> getRequests(long userId, long eventId) {
        return null;
    }

    @Override
    public ParticipationRequestDto confirmRequest(long userId, long eventId, long reqId) {
        return null;
    }

    @Override
    public ParticipationRequestDto rejectRequest(long userId, long eventId, long reqId) {
        return null;
    }
}
