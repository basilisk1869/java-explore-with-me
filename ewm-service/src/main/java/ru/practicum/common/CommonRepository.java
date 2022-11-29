package ru.practicum.common;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.repository.CompilationRepository;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.QEvent;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.AccessDeniedException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.request.model.QRequest;
import ru.practicum.request.model.Request;
import ru.practicum.request.model.RequestStatus;
import ru.practicum.request.repository.RequestRepository;
import ru.practicum.stats.StatsClient;
import ru.practicum.stats.ViewStats;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Slf4j
public class CommonRepository {

    @Autowired
    UserRepository userRepository;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    CompilationRepository compilationRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    RequestRepository requestRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    StatsClient statsClient;

    public Category getCategory(long catId) {
        return categoryRepository.findById(catId).orElseThrow(() -> {
            throw new NotFoundException("category is not found");
        });
    }

    public Compilation getCompilation(long compId) {
        return compilationRepository.findById(compId).orElseThrow(() -> {
            throw new NotFoundException("compilation is not found");
        });
    }

    public Event getEvent(long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() -> {
            throw new NotFoundException("event is not found");
        });
    }

    public Event getEventByUser(long userId, long eventId) {
        User initiator = getUser(userId);
        Event event = getEvent(eventId);
        if (event.getInitiator().equals(initiator)) {
            return event;
        } else {
            throw new AccessDeniedException("user has no access for this event");
        }
    }

    public User getUser(long catId) {
        return userRepository.findById(catId).orElseThrow(() -> {
            throw new NotFoundException("user is not found");
        });
    }

    public Request getRequest(long reqId) {
        return requestRepository.findById(reqId).orElseThrow(() -> {
            throw new NotFoundException("request is not found");
        });
    }

    public EventFullDto mapEventToFullDto(Event event) {
        EventFullDto eventFullDto = modelMapper.map(event, EventFullDto.class);
        eventFullDto.setConfirmedRequests(event.getRequests() == null ? 0L : event.getRequests().stream()
                .filter(request -> request.getStatus().equals(RequestStatus.CONFIRMED))
                .count());
        eventFullDto.setViews(0L);
        return eventFullDto;
    }

    public EventShortDto mapEventToShortDto(Event event) {
        EventShortDto eventShortDto = modelMapper.map(event, EventShortDto.class);
        eventShortDto.setConfirmedRequests(event.getRequests() == null ? 0L : event.getRequests().stream()
                .filter(request -> request.getStatus().equals(RequestStatus.CONFIRMED))
                .count());
        eventShortDto.setViews(0L);
        return eventShortDto;
    }

    public List<EventFullDto> getEventsFromQuery(JPAQuery<Tuple> jpaQuery, QEvent qEvent, QRequest qRequest) {
        return jpaQuery.fetch().stream()
                .map(tuple -> {
                    EventFullDto event = modelMapper.map(tuple.get(qEvent), EventFullDto.class);
                    event.setConfirmedRequests(tuple.get(qRequest.id.count()));
                    if (event.getConfirmedRequests() == null) {
                        event.setConfirmedRequests(0L);
                    }
                    return event;
                })
                .collect(Collectors.toList());
    }

    public void setViewsForEventList(List<EventFullDto> events, LocalDateTime rangeStart, LocalDateTime rangeEnd) {
        events.forEach(event -> event.setViews(0L));
        if (events.size() > 0 && rangeStart != null && rangeEnd != null) {
            List<String> uris = events.stream()
                    .map(event -> "/events/" + event.getId())
                    .collect(Collectors.toList());
            try {
                ResponseEntity<List<ViewStats>> viewStats = statsClient.getUrlViews(rangeStart, rangeEnd, uris, false);
                if (viewStats.getStatusCode() == HttpStatus.OK) {
                    Map<String, ViewStats> statsMap = viewStats.getBody().stream()
                            .collect(Collectors.toMap(ViewStats::getUri, Function.identity()));
                    events.forEach(event -> {
                        String key = "/events/" + event.getId();
                        if (statsMap.containsKey(key)) {
                            event.setViews(statsMap.get(key).getHits());
                        }
                    });
                }
            } catch (HttpClientErrorException e) {
                log.error("views not found : {} {} {}", rangeStart, rangeEnd, uris);
            }
        }
    }
}
