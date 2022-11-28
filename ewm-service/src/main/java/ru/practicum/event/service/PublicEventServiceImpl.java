package ru.practicum.event.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.category.model.Category;
import ru.practicum.common.DataRange;
import ru.practicum.common.GetterRepository;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventSort;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.event.repository.EventSpecification;
import ru.practicum.request.model.RequestStatus;
import ru.practicum.request.repository.RequestRepository;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PublicEventServiceImpl implements PublicEventService {

    @Autowired
    EventRepository eventRepository;

    @Autowired
    GetterRepository getterRepository;

    @Autowired
    RequestRepository requestRepository;

    @Override
    public List<EventShortDto> getEvents(String text, List<Integer> categoryIds, Boolean paid,
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
                    pageSort = Sort.by(Sort.Direction.ASC, "eventDate");
                    break;
                }
                case VIEWS: {
                    pageSort = Sort.by(Sort.Direction.ASC, "views");
                    break;
                }
            }
        }
        DataRange<User> dataRange = new DataRange<>(from, size, pageSort);
        return eventRepository.findAll(
                EventSpecification.getEventsByPublic(text, categories, paid, rangeStart, rangeEnd, onlyAvailable),
                dataRange.getPageable()).stream()
            .filter(event -> requestRepository.countByEventAndStatus(event, RequestStatus.CONFIRMED) < event.getParticipantLimit())
            .map(getterRepository::mapEventToShortDto)
            .collect(Collectors.toList());
    }

    @Override
    public EventFullDto getEvent(long eventId) {
        Event event = getterRepository.getEvent(eventId);
        return getterRepository.mapEventToFullDto(event);
    }
}
