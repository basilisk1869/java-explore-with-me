package ru.practicum.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ru.practicum.common.repository.CommonRepositoryImpl;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventSort;
import ru.practicum.event.model.EventState;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.AccessDeniedException;
import ru.practicum.stats.StatsClient;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.event.model.EventSort.VIEWS;

@Service
@Slf4j
@RequiredArgsConstructor
public class PublicEventServiceImpl implements PublicEventService {

    @Autowired
    private final CommonRepositoryImpl commonRepository;

    @Autowired
    private final StatsClient statsClient;

    @Autowired
    private final EventRepository eventRepository;

    @Override
    public @NotNull List<EventShortDto> getEvents(@Nullable String text,
                                                  @Nullable List<Long> categoryIds,
                                                  @Nullable Boolean paid,
                                                  @Nullable LocalDateTime rangeStart,
                                                  @Nullable LocalDateTime rangeEnd,
                                                  @Nullable Boolean onlyAvailable,
                                                  @Nullable String sort,
                                                  int from,
                                                  int size) {
        List<EventShortDto> events = eventRepository.getEvents(text, categoryIds, paid, rangeStart, rangeEnd,
                onlyAvailable, sort, from, size);
        statsClient.setViewsForEventShortDtoList(events, rangeStart, rangeEnd);
        // sort by views
        if (sort != null) {
            EventSort eventSort = EventSort.valueOf(sort);
            if (eventSort.equals(VIEWS)) {
                events = events.stream()
                        .sorted(Comparator.comparing(EventShortDto::getViews, Comparator.reverseOrder()))
                        .skip(from)
                        .limit(size)
                        .collect(Collectors.toList());
            }
        }
        return events;
    }

    @Override
    public @NotNull EventFullDto getEvent(long eventId) {
        Event event = commonRepository.getEvent(eventId);
        if (event.getState().equals(EventState.PUBLISHED)) {
            return commonRepository.mapEventToFullDto(event);
        } else {
            throw new AccessDeniedException("access to this event is forbidden");
        }
    }
}
