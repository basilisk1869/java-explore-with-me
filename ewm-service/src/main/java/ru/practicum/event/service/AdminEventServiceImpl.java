package ru.practicum.event.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.common.DataRange;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.event.repository.EventSpecification;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminEventServiceImpl implements AdminEventService {

    @Autowired
    EventRepository eventRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public List<EventFullDto> getEvents(List<Long> users, List<String> states, List<Long> categories,
                                        LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size) {
        DataRange<User> dataRange = new DataRange<>(from, size, Sort.by(Sort.Direction.ASC, "id"));
        return eventRepository.findAll(EventSpecification.getEventByAdmin(users, states, categories, rangeStart, rangeEnd),
                        dataRange.getPageable()).stream()
                .map(user -> modelMapper.map(user, EventFullDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto putEvent(long eventId, NewEventDto newEventDto) {
        Event event = modelMapper.map(newEventDto, Event.class);
        eventRepository.save(event);
        return modelMapper.map(event, EventFullDto.class);
    }

    @Override
    public EventFullDto publishEvent(long eventId) {
        Event event = eventRepository.findById(eventId).get();
        event.setState(EventState.PUBLISHED);
        eventRepository.save(event);
        return modelMapper.map(event, EventFullDto.class);
    }

    @Override
    public EventFullDto rejectEvent(long eventId) {
        Event event = eventRepository.findById(eventId).get();
        event.setState(EventState.REJECTED);
        eventRepository.save(event);
        return modelMapper.map(event, EventFullDto.class);
    }
}
