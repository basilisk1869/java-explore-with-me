package ru.practicum.event.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.category.model.Category;
import ru.practicum.common.GetterRepository;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventRequest;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.AccessDeniedException;
import ru.practicum.location.model.Location;
import ru.practicum.location.repository.LocationRepository;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.model.Request;
import ru.practicum.request.model.RequestStatus;
import ru.practicum.request.repository.RequestRepository;
import ru.practicum.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserEventServiceImpl implements UserEventService {

    @Autowired
    EventRepository eventRepository;

    @Autowired
    GetterRepository getterRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    LocationRepository locationRepository;

    @Autowired
    RequestRepository requestRepository;

    @Override
    public List<EventFullDto> getEvents(long userId, Integer from, Integer size) {
        User initiator = getterRepository.getUser(userId);
        return eventRepository.findAllByInitiator(initiator).stream()
            .map(event -> modelMapper.map(event, EventFullDto.class))
            .collect(Collectors.toList());
    }

    @Override
    public EventFullDto patchEvent(long userId, UpdateEventRequest updateEventRequest) {
        Event event = getterRepository.getEventByUser(userId, updateEventRequest.getEventId());
        modelMapper.map(updateEventRequest, event);
        return modelMapper.map(event, EventFullDto.class);
    }

    @Override
    public EventFullDto postEvent(long userId, NewEventDto newEventDto) {
        User user = getterRepository.getUser(userId);
        Category category = getterRepository.getCategory(newEventDto.getCategory());
        Location location = modelMapper.map(newEventDto.getLocation(), Location.class);
        locationRepository.save(location);
        Event event = modelMapper.map(newEventDto, Event.class);
        event.setInitiator(user);
        event.setCategory(category);
        event.setLocation(location);
        eventRepository.save(event);
        return modelMapper.map(event, EventFullDto.class);
    }

    @Override
    public EventFullDto getEvent(long userId, long eventId) {
        Event event = getterRepository.getEventByUser(userId, eventId);
        return modelMapper.map(event, EventFullDto.class);
    }

    @Override
    public EventFullDto cancelEvent(long userId, long eventId) {
        Event event = getterRepository.getEventByUser(userId, eventId);
        if (event.getState().equals(EventState.PENDING)) {
            event.setState(EventState.CANCELED);
            eventRepository.save(event);
            return modelMapper.map(event, EventFullDto.class);
        } else {
            throw new AccessDeniedException("event cannot be cancelled");
        }
    }

    @Override
    public List<ParticipationRequestDto> getRequests(long userId, long eventId) {
        Event event = getterRepository.getEventByUser(userId, eventId);
        return requestRepository.findAllByEvent(event).stream()
            .map(request -> modelMapper.map(request, ParticipationRequestDto.class))
            .collect(Collectors.toList());
    }

    @Override
    public ParticipationRequestDto confirmRequest(long userId, long eventId, long reqId) {
        Event event = getterRepository.getEventByUser(userId, eventId);
        Request request = getterRepository.getRequest(reqId);
        if (request.getEvent().equals(event)) {
            request.setStatus(RequestStatus.CONFIRMED);
            requestRepository.save(request);
            return modelMapper.map(request, ParticipationRequestDto.class);
        } else {
            throw new AccessDeniedException("request not for this event");
        }
    }

    @Override
    public ParticipationRequestDto rejectRequest(long userId, long eventId, long reqId) {
        Event event = getterRepository.getEventByUser(userId, eventId);
        Request request = getterRepository.getRequest(reqId);
        if (request.getEvent().equals(event)) {
            request.setStatus(RequestStatus.REJECTED);
            requestRepository.save(request);
            return modelMapper.map(request, ParticipationRequestDto.class);
        } else {
            throw new AccessDeniedException("request not for this event");
        }
    }
}
