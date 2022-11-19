package ru.practicum.event.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.NotFoundException;
import ru.practicum.location.model.Location;
import ru.practicum.location.repository.LocationRepository;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserEventServiceImpl implements UserEventService {

    @Autowired
    EventRepository eventRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    LocationRepository locationRepository;

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
        User user = getUser(userId);
        Category category = getCategory(newEventDto.getCategory());
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

    private User getUser(long userId) {
        return userRepository.findById(userId).orElseThrow(() -> {
            throw new NotFoundException("user is not found");
        });
    }

    private Category getCategory(long catId) {
        return categoryRepository.findById(catId).orElseThrow(() -> {
            throw new NotFoundException("category is not found");
        });
    }
}
