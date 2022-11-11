package ru.practicum.request.service;

import ru.practicum.request.dto.ParticipationRequestDto;

import java.util.List;

public interface UserRequestService {

    List<ParticipationRequestDto> getRequests(long userId);

    ParticipationRequestDto postRequest(long userId, long eventId);

    ParticipationRequestDto cancelRequest(long userId, long eventId);

}
