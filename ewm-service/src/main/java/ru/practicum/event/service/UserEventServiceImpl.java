package ru.practicum.event.service;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.category.model.Category;
import ru.practicum.common.CommonRepository;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventRequest;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;
import ru.practicum.event.model.QEvent;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.AccessDeniedException;
import ru.practicum.location.model.Location;
import ru.practicum.location.repository.LocationRepository;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.model.QRequest;
import ru.practicum.request.model.Request;
import ru.practicum.request.model.RequestStatus;
import ru.practicum.request.repository.RequestRepository;
import ru.practicum.user.model.User;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserEventServiceImpl implements UserEventService {

    @Autowired
    EventRepository eventRepository;

    @Autowired
    CommonRepository commonRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    LocationRepository locationRepository;

    @Autowired
    RequestRepository requestRepository;

    @Autowired
    EntityManager entityManager;

    @Override
    public List<EventFullDto> getEvents(long userId, Integer from, Integer size) {
        User initiator = commonRepository.getUser(userId);
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
        return commonRepository.getEventsFromQuery(jpaQuery, qEvent, qRequest);
    }

    @Override
    public EventFullDto patchEvent(long userId, UpdateEventRequest updateEventRequest) {
        Event event = commonRepository.getEventByUser(userId, updateEventRequest.getEventId());
        // изменить можно только отмененные события или события в состоянии ожидания модерации
        if (!event.getState().equals(EventState.CANCELED) && !event.getState().equals(EventState.PENDING)) {
            throw new AccessDeniedException("only canceled or pending events can be changed");
        }
        // если редактируется отменённое событие, то оно автоматически переходит в состояние ожидания модерации
        if ()


//
//        modelMapper.map(updateEventRequest, event);
        return commonRepository.mapEventToFullDto(event);
    }

    @Override
    public EventFullDto postEvent(long userId, NewEventDto newEventDto) {
        User user = commonRepository.getUser(userId);
        Category category = commonRepository.getCategory(newEventDto.getCategory());
        Location location = modelMapper.map(newEventDto.getLocation(), Location.class);
        locationRepository.save(location);
        Event event = modelMapper.map(newEventDto, Event.class);
        event.setInitiator(user);
        event.setCategory(category);
        event.setLocation(location);
        eventRepository.save(event);
        return commonRepository.mapEventToFullDto(event);
    }

    @Override
    public EventFullDto getEvent(long userId, long eventId) {
        Event event = commonRepository.getEventByUser(userId, eventId);
        return commonRepository.mapEventToFullDto(event);
    }

    @Override
    public EventFullDto cancelEvent(long userId, long eventId) {
        Event event = commonRepository.getEventByUser(userId, eventId);
        if (event.getState().equals(EventState.PENDING)) {
            event.setState(EventState.CANCELED);
            eventRepository.save(event);
            return commonRepository.mapEventToFullDto(event);
        } else {
            throw new AccessDeniedException("event cannot be cancelled");
        }
    }

    @Override
    public List<ParticipationRequestDto> getRequests(long userId, long eventId) {
        Event event = commonRepository.getEventByUser(userId, eventId);
        return requestRepository.findAllByEvent(event).stream()
            .map(request -> modelMapper.map(request, ParticipationRequestDto.class))
            .collect(Collectors.toList());
    }

    @Override
    public ParticipationRequestDto confirmRequest(long userId, long eventId, long reqId) {
        Event event = commonRepository.getEventByUser(userId, eventId);
        Request request = commonRepository.getRequest(reqId);
        if (request.getEvent().equals(event)) {
            request.setStatus(RequestStatus.CONFIRMED);
            requestRepository.save(request);
            return modelMapper.map(request, ParticipationRequestDto.class);
        } else {
            throw new AccessDeniedException("request not for this event");
        }
    }

    @Override
    public ParticipationRequestDto rejectRequest(long userId, long eventId, long reqId) {
        Event event = commonRepository.getEventByUser(userId, eventId);
        Request request = commonRepository.getRequest(reqId);
        if (request.getEvent().equals(event)) {
            request.setStatus(RequestStatus.REJECTED);
            requestRepository.save(request);
            return modelMapper.map(request, ParticipationRequestDto.class);
        } else {
            throw new AccessDeniedException("request not for this event");
        }
    }
}
