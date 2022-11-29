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
import org.springframework.stereotype.Service;
import ru.practicum.category.model.Category;
import ru.practicum.common.CommonRepository;
import ru.practicum.event.dto.AdminUpdateEventRequest;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;
import ru.practicum.event.model.QEvent;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.request.model.QRequest;
import ru.practicum.request.model.RequestStatus;
import ru.practicum.user.model.User;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminEventServiceImpl implements AdminEventService {

    @Autowired
    EventRepository eventRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    CommonRepository commonRepository;

    @Autowired
    EntityManager entityManager;

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
                    .map(commonRepository::getUser)
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
                    .map(commonRepository::getCategory)
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
        List<EventFullDto> events = commonRepository.getEventsFromQuery(jpaQuery, qEvent, qRequest);
        // views
        commonRepository.setViewsForEventList(events, rangeStart, rangeEnd);
        return events;
    }

    @Override
    public EventFullDto putEvent(long eventId, AdminUpdateEventRequest adminUpdateEventRequest) {
        Event event = commonRepository.getEvent(eventId);
        modelMapper.map(adminUpdateEventRequest, event);
        eventRepository.save(event);
        return commonRepository.mapEventToFullDto(event);
    }

    @Override
    public EventFullDto publishEvent(long eventId) {
        Event event = commonRepository.getEvent(eventId);
        event.setState(EventState.PUBLISHED);
        event.setPublishedOn(LocalDateTime.now());
        eventRepository.save(event);
        return commonRepository.mapEventToFullDto(event);
    }

    @Override
    public EventFullDto rejectEvent(long eventId) {
        Event event = commonRepository.getEvent(eventId);
        event.setState(EventState.CANCELED);
        eventRepository.save(event);
        return commonRepository.mapEventToFullDto(event);
    }
}
