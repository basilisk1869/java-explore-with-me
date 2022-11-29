package ru.practicum.request.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.common.CommonRepository;
import ru.practicum.event.model.Event;
import ru.practicum.exception.AccessDeniedException;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.model.Request;
import ru.practicum.request.model.RequestStatus;
import ru.practicum.request.repository.RequestRepository;
import ru.practicum.user.model.User;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserRequestServiceImpl implements UserRequestService {

    @Autowired
    RequestRepository requestRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    CommonRepository commonRepository;

    @Override
    public List<ParticipationRequestDto> getRequests(long userId) {
        User requester = commonRepository.getUser(userId);
        return requestRepository.findAllByRequester(requester).stream()
                .map(request -> modelMapper.map(request, ParticipationRequestDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public ParticipationRequestDto postRequest(long userId, long eventId) {
        User requester = commonRepository.getUser(userId);
        Event event = commonRepository.getEvent(eventId);
        Request request = new Request();
        request.setRequester(requester);
        request.setEvent(event);
        requestRepository.save(request);
        return modelMapper.map(request, ParticipationRequestDto.class);
    }

    @Override
    public ParticipationRequestDto cancelRequest(long userId, long requestId) {
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
