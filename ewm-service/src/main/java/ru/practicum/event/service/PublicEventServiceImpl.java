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
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventSort;
import ru.practicum.event.model.QEvent;
import ru.practicum.request.model.QRequest;
import ru.practicum.request.model.RequestStatus;
import ru.practicum.stats.StatsClient;
import ru.practicum.stats.ViewStats;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static ru.practicum.event.model.EventSort.EVENT_DATE;
import static ru.practicum.event.model.EventSort.VIEWS;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PublicEventServiceImpl implements PublicEventService {

    @Autowired
    GetterRepository getterRepository;

    @Autowired
    EntityManager entityManager;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    StatsClient statsClient;

    @Override
    public List<EventShortDto> getEvents(String text, List<Integer> categoryIds, Boolean paid,
                                         LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable,
                                         String sort, Integer from, Integer size) {
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        QEvent qEvent = QEvent.event;
        QRequest qRequest = QRequest.request;
        JPAQuery<Tuple> jpaQuery = jpaQueryFactory.select(qEvent, qRequest.id.count())
                .from(qEvent)
                .leftJoin(qRequest)
                .on(qRequest.event.eq(qEvent).and(qRequest.status.eq(RequestStatus.CONFIRMED)));
        // text
        if (text != null) {
            String preparedText = "%" + text.toUpperCase() + "%";
            jpaQuery.where(qEvent.annotation.upper().like(preparedText)
                    .or(qEvent.description.upper().like(preparedText)));
        }
        // categoryIds
        if (categoryIds != null) {
            List<Category> categories = categoryIds.stream()
                    .map(getterRepository::getCategory)
                    .collect(Collectors.toList());
            jpaQuery.where(qEvent.category.in(categories));
        }
        // paid
        if (paid != null) {
            jpaQuery.where(qEvent.paid.eq(paid));
        }
        // rangeStart & rangeEnd
        if (rangeStart == null && rangeEnd == null) {
            jpaQuery.where(qEvent.eventDate.goe(LocalDateTime.now()));
        } else {
            if (rangeStart != null) {
                jpaQuery.where(qEvent.eventDate.goe(rangeStart));
            }
            if (rangeEnd != null) {
                jpaQuery.where(qEvent.eventDate.loe(rangeEnd));
            }
        }
        // group by
        jpaQuery.groupBy(qEvent.id);
        // onlyAvailable
        if (onlyAvailable != null && onlyAvailable) {
            jpaQuery.where(qRequest.id.count().lt(qEvent.participantLimit));
        }
        // sort by event date
        if (sort != null) {
            EventSort eventSort = EventSort.valueOf(sort);
            if (eventSort.equals(EVENT_DATE)) {
                jpaQuery.orderBy(qEvent.eventDate.asc());
                // from
                jpaQuery.offset(from);
                // size
                jpaQuery.limit(size);
            }
        } else {
            // from
            jpaQuery.offset(from);
            // size
            jpaQuery.limit(size);
        }
        // mapping
        List<EventShortDto> events = jpaQuery.fetch().stream()
                .map(tuple -> {
                    EventShortDto event = modelMapper.map(tuple.get(qEvent), EventShortDto.class);
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
    public EventFullDto getEvent(long eventId) {
        Event event = getterRepository.getEvent(eventId);
        return getterRepository.mapEventToFullDto(event);
    }
}
