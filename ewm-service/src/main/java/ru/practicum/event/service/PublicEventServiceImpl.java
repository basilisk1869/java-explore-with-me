package ru.practicum.event.service;

import java.util.stream.Collectors;
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
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventSort;
import ru.practicum.event.model.EventState;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.event.repository.EventSpecification;
import ru.practicum.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import ru.practicum.user.model.User;

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
    public List<EventFullDto> getEvents(String text, List<Integer> categoryIds, Boolean paid,
        LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable, String sort,
        Integer from, Integer size) {
        List<Category> categories = (categoryIds == null ? List.of() : categoryIds.stream()
            .map(getterRepository::getCategory)
            .collect(Collectors.toList()));
        Sort pageSort = Sort.by(Sort.Direction.ASC, "id");
        if (sort != null) {
            EventSort eventSort = EventSort.valueOf(sort);
            switch (eventSort) {
                case EVENT_DATE: {
                    pageSort = Sort.by(Sort.Direction.ASC, "event_date");
                    break;
                }
                case VIEWS: {
//                    pageSort = Sort.by(Sort.Direction.ASC, "views");
                    break;
                }
            }
        };
        DataRange<User> dataRange = new DataRange<>(from, size, pageSort);
        return eventRepository.findAll(
                EventSpecification.getEventsByPublic(text, categories, paid, rangeStart, rangeEnd,
                    onlyAvailable),
                dataRange.getPageable()).stream()
            .map(user -> modelMapper.map(user, EventFullDto.class))
            .collect(Collectors.toList());
    }

    @Override
    public EventFullDto getEvent(long eventId) {
        Event event = getterRepository.getEvent(eventId);
        return modelMapper.map(event, EventFullDto.class);
    }
}
