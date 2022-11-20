package ru.practicum.request.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.common.GetterRepository;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.NotFoundException;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.model.Request;
import ru.practicum.request.repository.RequestRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.util.List;
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
    GetterRepository getterRepository;

    @Override
    public List<ParticipationRequestDto> getRequests(long userId) {
        User requester = getterRepository.getUser(userId);
        return requestRepository.findAllByRequester(requester).stream()
                .map(request -> modelMapper.map(request, ParticipationRequestDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public ParticipationRequestDto postRequest(long userId, long eventId) {
        User requester = getterRepository.getUser(userId);
        Event event = getterRepository.getEvent(eventId);
        Request request = new Request();
        request.setRequester(requester);
        request.setEvent(event);
        requestRepository.save(request);
        return modelMapper.map(request, ParticipationRequestDto.class);
    }

    @Override
    public ParticipationRequestDto cancelRequest(long userId, long eventId) {
        User requester = getterRepository.getUser(userId);
        Event event = getterRepository.getEvent(eventId);
        Request request = requestRepository.findByRequesterAndEvent(requester, event)
            .orElseThrow(() -> {
                throw new NotFoundException("user is not found");
            });
        return modelMapper.map(request, ParticipationRequestDto.class);
    }
}
