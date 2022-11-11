package ru.practicum.request.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.repository.RequestRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserRequestServiceImpl implements UserRequestService {

    @Autowired
    RequestRepository requestRepository;

    @Override
    public List<ParticipationRequestDto> getRequests(long userId) {
        return null;
    }

    @Override
    public ParticipationRequestDto postRequest(long userId, long eventId) {
        return null;
    }

    @Override
    public ParticipationRequestDto cancelRequest(long userId, long eventId) {
        return null;
    }
}
