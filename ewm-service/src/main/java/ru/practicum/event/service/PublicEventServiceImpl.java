package ru.practicum.event.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.common.GetterRepository;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PublicEventServiceImpl implements PublicEventService {

    @Autowired
    EventRepository eventRepository;

    @Autowired
    GetterRepository getterRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public List<EventFullDto> getEvents(String text, List<Integer> categories, Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable, String sort, Integer from, Integer size) {
        return null;
    }

    @Override
    public EventFullDto getEvent(long eventId) {
        Event event = getterRepository.getEvent(eventId);
        return modelMapper.map(event, EventFullDto.class);
    }
}
