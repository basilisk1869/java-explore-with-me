package ru.practicum.event.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.category.model.Category;
import ru.practicum.common.DataRange;
import ru.practicum.common.GetterRepository;
import ru.practicum.event.dto.AdminUpdateEventRequest;
import ru.practicum.event.dto.EventFullDto;
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

    @Autowired
    GetterRepository getterRepository;

    @Override
    public List<EventFullDto> getEvents(List<Long> userIds, List<String> stateIds, List<Long> categoryIds,
                                        LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size) {
        DataRange<User> dataRange = new DataRange<>(from, size, Sort.by(Sort.Direction.ASC, "id"));
        List<User> users = (userIds == null ? List.of() : userIds.stream()
            .map(getterRepository::getUser)
            .collect(Collectors.toList()));
        List<EventState> states = (stateIds == null ? List.of() : stateIds.stream()
            .map(EventState::valueOf)
            .collect(Collectors.toList()));
        List<Category> categories = (categoryIds == null ? List.of() : categoryIds.stream()
            .map(getterRepository::getCategory)
            .collect(Collectors.toList()));
        return eventRepository.findAll(EventSpecification.getEventsByAdmin(users, states, categories, rangeStart, rangeEnd),
                        dataRange.getPageable()).stream()
                .map(getterRepository::mapEventToFullDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto putEvent(long eventId, AdminUpdateEventRequest adminUpdateEventRequest) {
        Event event = getterRepository.getEvent(eventId);
        modelMapper.map(adminUpdateEventRequest, event);
        eventRepository.save(event);
        return modelMapper.map(event, EventFullDto.class);
    }

    @Override
    public EventFullDto publishEvent(long eventId) {
        Event event = getterRepository.getEvent(eventId);
        event.setState(EventState.PUBLISHED);
        event.setPublishedOn(LocalDateTime.now());
        eventRepository.save(event);
        return modelMapper.map(event, EventFullDto.class);
    }

    @Override
    public EventFullDto rejectEvent(long eventId) {
        Event event = getterRepository.getEvent(eventId);
        event.setState(EventState.CANCELED);
        eventRepository.save(event);
        return modelMapper.map(event, EventFullDto.class);
    }
}
