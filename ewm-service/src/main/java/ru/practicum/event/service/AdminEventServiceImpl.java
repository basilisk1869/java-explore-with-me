package ru.practicum.event.service;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import ru.practicum.category.model.Category;
import ru.practicum.common.GetterRepository;
import ru.practicum.event.dto.AdminUpdateEventRequest;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;
import ru.practicum.event.model.QEvent;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.request.model.QRequest;
import ru.practicum.request.model.Request;
import ru.practicum.request.model.RequestStatus;
import ru.practicum.request.repository.RequestRepository;
import ru.practicum.stats.StatsClient;
import ru.practicum.stats.ViewStats;
import ru.practicum.user.model.User;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE/*, makeFinal = true*/)
public class AdminEventServiceImpl implements AdminEventService {

    @Autowired
    EventRepository eventRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    GetterRepository getterRepository;

    @Autowired
    EntityManager entityManager;

    @Autowired
    StatsClient statsClient;

    @Autowired
    RequestRepository requestRepository;

    @Override
    public List<EventFullDto> getEvents(List<Long> userIds, List<String> stateIds, List<Long> categoryIds,
                                        LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size) {
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        QEvent qEvent = QEvent.event;
        QRequest qRequest = QRequest.request;
        JPAQuery<Tuple> jpaQuery = jpaQueryFactory.select(qEvent, qRequest.id.count())
                .from(qEvent)
                .leftJoin(qRequest)
                .on(qRequest.event.eq(qEvent).and(qRequest.status.eq(RequestStatus.CONFIRMED)));
        // userIds
        if (userIds != null) {
            List<User> users = userIds.stream()
                    .map(getterRepository::getUser)
                    .collect(Collectors.toList());
            jpaQuery.where(qEvent.initiator.in(users));
        }
        // stateIds
        if (stateIds != null) {
            List<EventState> states = stateIds.stream()
                    .map(EventState::valueOf)
                    .collect(Collectors.toList());
            jpaQuery.where(qEvent.state.in(states));
        }
        // categoryIds
        if (categoryIds != null) {
            List<Category> categories = categoryIds.stream()
                    .map(getterRepository::getCategory)
                    .collect(Collectors.toList());
            jpaQuery.where(qEvent.category.in(categories));
        }
        // rangeStart
        if (rangeStart != null) {
            jpaQuery.where(qEvent.eventDate.goe(rangeStart));
        }
        // rangeEnd
        if (rangeEnd != null) {
            jpaQuery.where(qEvent.eventDate.loe(rangeEnd));
        }
        // group by
        jpaQuery.groupBy(qEvent.id);
        // from
        jpaQuery.offset(from);
        // size
        jpaQuery.limit(size);
        // mapping
        List<Request> test2 = requestRepository.findAll();
        List<EventFullDto> events = jpaQuery.fetch().stream()
                .map(tuple -> {
                    EventFullDto event = modelMapper.map(tuple.get(qEvent), EventFullDto.class);
                    event.setConfirmedRequests(tuple.get(qRequest.id.count()));
                    if (event.getConfirmedRequests() == null) {
                        event.setConfirmedRequests(0L);
                    }
                    return event;
                })
                .collect(Collectors.toList());
        // views
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
        return events;
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
