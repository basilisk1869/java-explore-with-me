package ru.practicum.event.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.category.model.Category;
import ru.practicum.common.repository.CommonRepositoryImpl;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventRequest;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.AccessDeniedException;
import ru.practicum.location.model.Location;
import ru.practicum.location.repository.LocationRepository;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.model.Request;
import ru.practicum.request.model.RequestStatus;
import ru.practicum.request.repository.RequestRepository;
import ru.practicum.user.model.User;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserEventServiceImpl implements UserEventService {

    @Autowired
    private final EventRepository eventRepository;

    @Autowired
    private final CommonRepositoryImpl commonRepository;

    @Autowired
    private final ModelMapper modelMapper;

    @Autowired
    private final LocationRepository locationRepository;

    @Autowired
    private final RequestRepository requestRepository;

    @Override
    public @NotNull List<EventFullDto> getEvents(long userId, int from, int size) {
        User initiator = commonRepository.getUser(userId);
        return eventRepository.getEvents(initiator, from, size);
    }

    @Override
    public @NotNull EventFullDto patchEvent(long userId, @NotNull UpdateEventRequest updateEventRequest) {
        Event event = commonRepository.getEventByUser(userId, updateEventRequest.getEventId());
        // изменить можно только отмененные события или события в состоянии ожидания модерации
        if (!event.getState().equals(EventState.CANCELED) && !event.getState().equals(EventState.PENDING)) {
            throw new AccessDeniedException("only canceled or pending events can be changed");
        }
        // дата и время на которые намечено событие не может быть раньше, чем через два часа от текущего момента
        if (updateEventRequest.getEventDate() != null
                && updateEventRequest.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new AccessDeniedException("event date is too early");
        }
        // если редактируется отменённое событие, то оно автоматически переходит в состояние ожидания модерации
        if (event.getState().equals(EventState.CANCELED)) {
            event.setState(EventState.PENDING);
        }
        modelMapper.map(updateEventRequest, event);
        eventRepository.save(event);
        return commonRepository.mapEventToFullDto(event);
    }

    @Override
    public @NotNull EventFullDto postEvent(long userId, @NotNull NewEventDto newEventDto) {
        User user = commonRepository.getUser(userId);
        Category category = commonRepository.getCategory(newEventDto.getCategory());
        if (newEventDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new AccessDeniedException("event date is too early");
        }
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
    public @NotNull EventFullDto getEvent(long userId, long eventId) {
        Event event = commonRepository.getEventByUser(userId, eventId);
        return commonRepository.mapEventToFullDto(event);
    }

    @Override
    public @NotNull EventFullDto cancelEvent(long userId, long eventId) {
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
    public @NotNull List<ParticipationRequestDto> getRequests(long userId, long eventId) {
        Event event = commonRepository.getEventByUser(userId, eventId);
        return requestRepository.findAllByEvent(event).stream()
            .map(request -> modelMapper.map(request, ParticipationRequestDto.class))
            .collect(Collectors.toList());
    }

    @Override
    public @NotNull ParticipationRequestDto confirmRequest(long userId, long eventId, long reqId) {
        Event event = commonRepository.getEventByUser(userId, eventId);
        Request request = commonRepository.getRequest(reqId);
        if (request.getEvent().equals(event)) {
            // нельзя подтвердить заявку, если уже достигнут лимит по заявкам на данное событие
            if (event.getParticipantLimit() > 0
                    && event.getParticipantLimit() == commonRepository.getConfirmedRequests(event)) {
                throw new AccessDeniedException("participation limit is reached");
            }
            request.setStatus(RequestStatus.CONFIRMED);
            requestRepository.save(request);
            // если при подтверждении данной заявки, лимит заявок для события исчерпан,
            // то все неподтверждённые заявки необходимо отклонить
            if (event.getParticipantLimit() > 0
                    && event.getParticipantLimit() == commonRepository.getConfirmedRequests(event)) {
                requestRepository.findAllByEvent(event).stream()
                        .filter(anotherRequest -> anotherRequest.getStatus().equals(RequestStatus.PENDING))
                        .forEach(anotherRequest -> {
                            anotherRequest.setStatus(RequestStatus.CANCELED);
                            requestRepository.save(anotherRequest);
                        });
            }
            return modelMapper.map(request, ParticipationRequestDto.class);
        } else {
            throw new AccessDeniedException("request is not for this event");
        }
    }

    @Override
    public @NotNull ParticipationRequestDto rejectRequest(long userId, long eventId, long reqId) {
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
