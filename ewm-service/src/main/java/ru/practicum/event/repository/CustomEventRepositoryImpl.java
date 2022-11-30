package ru.practicum.event.repository;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.model.EventSort;
import ru.practicum.event.model.EventState;
import ru.practicum.event.model.QEvent;
import ru.practicum.request.model.QRequest;
import ru.practicum.request.model.RequestStatus;
import ru.practicum.user.model.User;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.event.model.EventSort.EVENT_DATE;

public class CustomEventRepositoryImpl implements CustomEventRepository {

    @Autowired
    EntityManager entityManager;

    @Autowired
    ModelMapper modelMapper;

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
            jpaQuery.where(qEvent.initiator.id.in(userIds));
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
            jpaQuery.where(qEvent.category.id.in(categoryIds));
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
        return getEventFullDtoFromQuery(jpaQuery, qEvent, qRequest);
    }

    @Override
    public List<EventFullDto> getEvents(User initiator, Integer from, Integer size) {
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        QEvent qEvent = QEvent.event;
        QRequest qRequest = QRequest.request;
        JPAQuery<Tuple> jpaQuery = jpaQueryFactory.select(qEvent, qRequest.id.count())
                .from(qEvent)
                .leftJoin(qRequest)
                .on(qRequest.event.eq(qEvent).and(qRequest.status.eq(RequestStatus.CONFIRMED)))
                .where(qEvent.initiator.eq(initiator))
                .groupBy(qEvent.id)
                .orderBy(qEvent.id.asc())
                .offset(from)
                .limit(size);
        return getEventFullDtoFromQuery(jpaQuery, qEvent, qRequest);
    }

    @Override
    public List<EventShortDto> getEvents(String text, List<Long> categoryIds, Boolean paid,
                                         LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable,
                                         String sort, Integer from, Integer size) {
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        QEvent qEvent = QEvent.event;
        QRequest qRequest = QRequest.request;
        JPAQuery<Tuple> jpaQuery = jpaQueryFactory.select(qEvent, qRequest.id.count())
                .from(qEvent)
                .leftJoin(qRequest)
                .on(qRequest.event.eq(qEvent).and(qRequest.status.eq(RequestStatus.CONFIRMED)))
                .where(qEvent.state.eq(EventState.PUBLISHED));
        // text
        if (text != null) {
            String preparedText = "%" + text.toUpperCase() + "%";
            jpaQuery.where(qEvent.annotation.upper().like(preparedText)
                    .or(qEvent.description.upper().like(preparedText)));
        }
        // categoryIds
        if (categoryIds != null) {
            jpaQuery.where(qEvent.category.id.in(categoryIds));
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
        return getEventShortDtoFromQuery(jpaQuery, qEvent, qRequest);
    }

    private List<EventFullDto> getEventFullDtoFromQuery(JPAQuery<Tuple> jpaQuery, QEvent qEvent, QRequest qRequest) {
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

    private List<EventShortDto> getEventShortDtoFromQuery(JPAQuery<Tuple> jpaQuery, QEvent qEvent, QRequest qRequest) {
        return jpaQuery.fetch().stream()
                .map(tuple -> {
                    EventShortDto event = modelMapper.map(tuple.get(qEvent), EventShortDto.class);
                    event.setConfirmedRequests(tuple.get(qRequest.id.count()));
                    if (event.getConfirmedRequests() == null) {
                        event.setConfirmedRequests(0L);
                    }
                    return event;
                })
                .collect(Collectors.toList());
    }

}
