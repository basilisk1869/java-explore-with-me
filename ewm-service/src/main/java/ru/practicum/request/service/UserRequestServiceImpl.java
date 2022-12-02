package ru.practicum.request.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.common.repository.CommonRepositoryImpl;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;
import ru.practicum.exception.AccessDeniedException;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.model.Request;
import ru.practicum.request.model.RequestStatus;
import ru.practicum.request.repository.RequestRepository;
import ru.practicum.user.model.User;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserRequestServiceImpl implements UserRequestService {

    @Autowired
    private final RequestRepository requestRepository;

    @Autowired
    private final ModelMapper modelMapper;

    @Autowired
    private final CommonRepositoryImpl commonRepository;

    @Override
    public @NotNull List<ParticipationRequestDto> getRequests(long userId) {
        User requester = commonRepository.getUser(userId);
        return requestRepository.findAllByRequester(requester).stream()
                .map(request -> modelMapper.map(request, ParticipationRequestDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public @NotNull ParticipationRequestDto postRequest(long userId, long eventId) {
        User requester = commonRepository.getUser(userId);
        Event event = commonRepository.getEvent(eventId);
        // нельзя добавить повторный запрос
        if (requestRepository.findByRequesterAndEvent(requester, event).isPresent()) {
            throw new AccessDeniedException("request is already exists");
        }
        // инициатор события не может добавить запрос на участие в своём событии
        if (requester.equals(event.getInitiator())) {
            throw new AccessDeniedException("user is event initiator");
        }
        // нельзя участвовать в неопубликованном событии
        if (!Objects.equals(event.getState(), EventState.PUBLISHED)) {
            throw new AccessDeniedException("cannot participate in unpublished event");
        }
        Request request = new Request();
        // если количество участников не ограничено
        if (event.getParticipantLimit() == 0) {
            request.setStatus(RequestStatus.CONFIRMED);
        } else if (!event.getRequestModeration()) {
            // усли подтверждения не требуется
            if (commonRepository.getConfirmedRequests(event) < event.getParticipantLimit()) {
                request.setStatus(RequestStatus.CONFIRMED);
            } else { // если у события достигнут лимит запросов на участие - необходимо вернуть ошибку
                throw new AccessDeniedException("participation limit is reached");
            }
        }
        request.setRequester(requester);
        request.setEvent(event);
        requestRepository.save(request);
        return modelMapper.map(request, ParticipationRequestDto.class);
    }

    @Override
    public @NotNull ParticipationRequestDto cancelRequest(long userId, long requestId) {
        User requester = commonRepository.getUser(userId);
        Request request = commonRepository.getRequest(requestId);
        if (Objects.equals(request.getRequester(), requester)) {
            request.setStatus(RequestStatus.CANCELED);
            requestRepository.save(request);
            return  modelMapper.map(request, ParticipationRequestDto.class);
        } else {
            throw new AccessDeniedException("user has no access for this request");
        }
    }

}
