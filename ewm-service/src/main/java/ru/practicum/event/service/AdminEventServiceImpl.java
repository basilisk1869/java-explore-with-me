package ru.practicum.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.common.CommonRepository;
import ru.practicum.event.dto.AdminUpdateEventRequest;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.AccessDeniedException;
import ru.practicum.stats.StatsClient;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminEventServiceImpl implements AdminEventService {

    @Autowired
    private final EventRepository eventRepository;

    @Autowired
    private final ModelMapper modelMapper;

    @Autowired
    private final CommonRepository commonRepository;

    @Autowired
    private final StatsClient statsClient;

    @Override
    public List<EventFullDto> getEvents(List<Long> userIds, List<String> stateIds, List<Long> categoryIds,
                                        LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size) {
        List<EventFullDto> events = eventRepository.getEvents(userIds, stateIds, categoryIds, rangeStart, rangeEnd,
                from, size);
        statsClient.setViewsForEventFullDtoList(events, rangeStart, rangeEnd);
        return events;
    }

    @Override
    public EventFullDto putEvent(long eventId, AdminUpdateEventRequest adminUpdateEventRequest) {
        Event event = commonRepository.getEvent(eventId);
        modelMapper.map(adminUpdateEventRequest, event);
        eventRepository.save(event);
        return commonRepository.mapEventToFullDto(event);
    }

    @Override
    public EventFullDto publishEvent(long eventId) {
        Event event = commonRepository.getEvent(eventId);
        // дата начала события должна быть не ранее чем за час от даты публикации
        if (event.getEventDate().isBefore(LocalDateTime.now())) {
            throw new AccessDeniedException("event is published too late");
        }
        // событие должно быть в состоянии ожидания публикации
        if (!event.getState().equals(EventState.PENDING)) {
            throw new AccessDeniedException("event state should be pending");
        }
        event.setState(EventState.PUBLISHED);
        event.setPublishedOn(LocalDateTime.now());
        eventRepository.save(event);
        return commonRepository.mapEventToFullDto(event);
    }

    @Override
    public EventFullDto rejectEvent(long eventId) {
        Event event = commonRepository.getEvent(eventId);
        // событие не должно быть опубликовано
        if (event.getState().equals(EventState.PUBLISHED)) {
            throw new AccessDeniedException("cannot reject published event");
        }
        event.setState(EventState.CANCELED);
        eventRepository.save(event);
        return commonRepository.mapEventToFullDto(event);
    }
}
